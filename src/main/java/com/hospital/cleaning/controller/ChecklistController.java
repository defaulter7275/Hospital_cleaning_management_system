package com.hospital.cleaning.controller;

import com.hospital.cleaning.dto.ChecklistDTO;
import com.hospital.cleaning.model.User;
import com.hospital.cleaning.repository.AreaRepository;
import com.hospital.cleaning.repository.FloorRepository;
import com.hospital.cleaning.repository.UserRepository;
import com.hospital.cleaning.service.ChecklistService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/checklists")
public class ChecklistController {
    
    private final ChecklistService checklistService;
    private final FloorRepository floorRepository;
    private final AreaRepository areaRepository;
    private final UserRepository userRepository;
    
    public ChecklistController(ChecklistService checklistService,
                               FloorRepository floorRepository,
                               AreaRepository areaRepository,
                               UserRepository userRepository) {
        this.checklistService = checklistService;
        this.floorRepository = floorRepository;
        this.areaRepository = areaRepository;
        this.userRepository = userRepository;
    }
    
    // LIST: Staff sees only their assigned checklists
    @GetMapping
    public String listChecklists(Model model, Authentication authentication) {
        User currentUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<ChecklistDTO> checklists;
        
        // Staff can only see their assigned checklists
        if (currentUser.getRole() == User.UserRole.STAFF) {
            checklists = checklistService.getChecklistsByEmployee(currentUser.getId());
            model.addAttribute("title", "My Assigned Checklists");
        } else {
            // Admin and Manager see all checklists
            checklists = checklistService.getAllChecklistDTOs();
            model.addAttribute("title", "All Checklists");
        }
        
        model.addAttribute("checklists", checklists);
        model.addAttribute("currentUser", currentUser);
        return "checklists/list";
    }
    
    // VIEW: Staff can view only their assigned checklists
    @GetMapping("/{id}")
    public String viewChecklist(@PathVariable Long id, Model model, Authentication authentication) {
        User currentUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        ChecklistDTO checklist = checklistService.getChecklistDTOById(id);
        
        // Staff can only view their own assigned checklists
        if (currentUser.getRole() == User.UserRole.STAFF && 
            !checklist.getAssignedToId().equals(currentUser.getId())) {
            throw new RuntimeException("Access denied: This checklist is not assigned to you");
        }
        
        model.addAttribute("checklist", checklist);
        model.addAttribute("currentUser", currentUser);
        return "checklists/view";
    }
    
    // CREATE: Only ADMIN & MANAGER
    @GetMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String showCreateForm(Model model) {
        model.addAttribute("checklist", new ChecklistDTO());
        model.addAttribute("floors", floorRepository.findAll());
        model.addAttribute("areas", areaRepository.findAll());
        model.addAttribute("users", userRepository.findAll());
        return "checklists/create";
    }
    
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String createChecklist(@ModelAttribute ChecklistDTO checklistDTO,
                                  RedirectAttributes redirectAttributes) {
        try {
            checklistService.createChecklist(checklistDTO);
            redirectAttributes.addFlashAttribute("success", "Checklist created successfully!");
            return "redirect:/checklists";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create checklist: " + e.getMessage());
            return "redirect:/checklists/create";
        }
    }
    
    // EDIT: Only ADMIN & MANAGER
    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String showEditForm(@PathVariable Long id, Model model) {
        ChecklistDTO checklist = checklistService.getChecklistDTOById(id);
        model.addAttribute("checklist", checklist);
        model.addAttribute("floors", floorRepository.findAll());
        model.addAttribute("areas", areaRepository.findAll());
        model.addAttribute("users", userRepository.findAll());
        return "checklists/edit";
    }
    
    @PostMapping("/{id}/edit")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String updateChecklist(@PathVariable Long id,
                                  @ModelAttribute ChecklistDTO checklistDTO,
                                  RedirectAttributes redirectAttributes) {
        try {
            checklistService.updateChecklist(id, checklistDTO);
            redirectAttributes.addFlashAttribute("success", "Checklist updated successfully!");
            return "redirect:/checklists";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update checklist: " + e.getMessage());
            return "redirect:/checklists/" + id + "/edit";
        }
    }
    
    // DELETE: Only ADMIN
    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteChecklist(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            checklistService.deleteChecklist(id);
            redirectAttributes.addFlashAttribute("success", "Checklist deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete checklist: " + e.getMessage());
        }
        return "redirect:/checklists";
    }
    
    // UPDATE STATUS: Staff can update checklist status
    @PostMapping("/{id}/status")
    public String updateChecklistStatus(@PathVariable Long id,
                                       @RequestParam String status,
                                       Authentication authentication,
                                       RedirectAttributes redirectAttributes) {
        try {
            User currentUser = userRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            ChecklistDTO checklist = checklistService.getChecklistDTOById(id);
            
            // Staff can only update their assigned checklists
            if (currentUser.getRole() == User.UserRole.STAFF && 
                !checklist.getAssignedToId().equals(currentUser.getId())) {
                throw new RuntimeException("Access denied");
            }
            
            checklistService.updateChecklistStatus(id, status);
            redirectAttributes.addFlashAttribute("success", "Status updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update status: " + e.getMessage());
        }
        return "redirect:/checklists/" + id;
    }
}
