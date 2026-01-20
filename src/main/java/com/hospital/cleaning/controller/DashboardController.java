package com.hospital.cleaning.controller;

import com.hospital.cleaning.dto.ChecklistDTO;
import com.hospital.cleaning.model.*;
import com.hospital.cleaning.repository.*;
import com.hospital.cleaning.service.ChecklistService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class DashboardController {
    
    private final UserRepository userRepository;
    private final FloorRepository floorRepository;
    private final ChecklistRepository checklistRepository;
    private final ChecklistService checklistService;
    
    public DashboardController(UserRepository userRepository,
                              FloorRepository floorRepository,
                              ChecklistRepository checklistRepository,
                              ChecklistService checklistService) {
        this.userRepository = userRepository;
        this.floorRepository = floorRepository;
        this.checklistRepository = checklistRepository;
        this.checklistService = checklistService;
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        User currentUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Hospital information
        model.addAttribute("hospitalName", "Shree Ji Hospital");
        
        // Get user's assigned checklists
        List<ChecklistDTO> userChecklists;
        if (currentUser.getRole() == User.UserRole.STAFF) {
            userChecklists = checklistService.getChecklistsByEmployee(currentUser.getId());
        } else {
            userChecklists = checklistService.getAllChecklistDTOs();
        }
        
        // Statistics
        model.addAttribute("totalCleaningTasks", userChecklists.size());
        model.addAttribute("completedToday", userChecklists.stream()
                .filter(c -> c.getStatus().equals("COMPLETED"))
                .count());
        model.addAttribute("totalChecklists", userChecklists.size());
        model.addAttribute("pendingChecklists", userChecklists.stream()
                .filter(c -> c.getStatus().equals("PENDING"))
                .count());
        model.addAttribute("totalEmployees", userRepository.count());
        model.addAttribute("onDutyEmployees", userRepository.countByActive(true));
        
        // Notifications (placeholder)
        model.addAttribute("notifications", new ArrayList<>());
        model.addAttribute("notificationCount", 0);
        model.addAttribute("urgentNotifications", 0);
        
        // Today's activities - Show pending checklists as interactive tasks
        List<ChecklistDTO> todayActivities = userChecklists.stream()
                .filter(c -> c.getStatus().equals("PENDING") || c.getStatus().equals("IN_PROGRESS"))
                .limit(5)
                .collect(Collectors.toList());
        model.addAttribute("todayActivities", todayActivities);
        
        // Floors with completion percentage
        List<Floor> floors = floorRepository.findAll();
        List<Map<String, Object>> floorsWithCompletion = floors.stream()
                .map(floor -> {
                    Map<String, Object> floorMap = new HashMap<>();
                    floorMap.put("id", floor.getId());
                    floorMap.put("name", floor.getName());
                    floorMap.put("completionPercentage", calculateFloorCompletionPercentage(floor));
                    floorMap.put("areas", floor.getAreas());
                    return floorMap;
                })
                .collect(Collectors.toList());
        
        model.addAttribute("floors", floorsWithCompletion);
        
        return "dashboard";
    }
    
    private double calculateFloorCompletionPercentage(Floor floor) {
        List<Checklist> floorChecklists = checklistRepository.findAll().stream()
                .filter(c -> c.getFloor() != null && c.getFloor().getId().equals(floor.getId()))
                .collect(Collectors.toList());
        
        if (floorChecklists.isEmpty()) {
            return 0.0;
        }
        
        long completedCount = floorChecklists.stream()
                .filter(c -> c.getStatus() == Checklist.ChecklistStatus.COMPLETED || 
                           c.getStatus() == Checklist.ChecklistStatus.SUBMITTED)
                .count();
        
        return Math.round((completedCount * 100.0) / floorChecklists.size());
    }
}
