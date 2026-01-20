package com.hospital.cleaning.service;

import com.hospital.cleaning.dto.ChecklistDTO;
import com.hospital.cleaning.model.*;
import com.hospital.cleaning.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChecklistService {
    
    private final ChecklistRepository checklistRepository;
    private final TaskRepository taskRepository;
    private final AreaRepository areaRepository;
    private final UserRepository userRepository;
    
    public ChecklistService(ChecklistRepository checklistRepository, 
                           TaskRepository taskRepository,
                           AreaRepository areaRepository,
                           UserRepository userRepository) {
        this.checklistRepository = checklistRepository;
        this.taskRepository = taskRepository;
        this.areaRepository = areaRepository;
        this.userRepository = userRepository;
    }
    
    public List<ChecklistDTO> getAllChecklistDTOs() {
        return checklistRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public ChecklistDTO getChecklistDTOById(Long id) {
        Checklist checklist = checklistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Checklist not found"));
        return convertToDTO(checklist);
    }
    
    public List<Checklist> getAllChecklists() {
        return checklistRepository.findAll();
    }
    
    public Checklist getChecklistById(Long id) {
        return checklistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Checklist not found"));
    }
    
    @Transactional
    public ChecklistDTO createChecklist(ChecklistDTO dto) {
        Checklist checklist = new Checklist();
        checklist.setName(dto.getName());
        
        if (dto.getAreaId() != null) {
            Area area = areaRepository.findById(dto.getAreaId())
                    .orElseThrow(() -> new RuntimeException("Area not found"));
            checklist.setArea(area);
            checklist.setFloor(area.getFloor());
        }
        
        if (dto.getAssignedToId() != null) {
            User user = userRepository.findById(dto.getAssignedToId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            checklist.setAssignedTo(user);
        }
        
        checklist.setStatus(Checklist.ChecklistStatus.PENDING);
        Checklist saved = checklistRepository.save(checklist);
        return convertToDTO(saved);
    }
    
    @Transactional
    public ChecklistDTO updateChecklist(Long id, ChecklistDTO dto) {
        Checklist checklist = getChecklistById(id);
        checklist.setName(dto.getName());
        
        if (dto.getAreaId() != null) {
            Area area = areaRepository.findById(dto.getAreaId())
                    .orElseThrow(() -> new RuntimeException("Area not found"));
            checklist.setArea(area);
        }
        
        if (dto.getAssignedToId() != null) {
            User user = userRepository.findById(dto.getAssignedToId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            checklist.setAssignedTo(user);
        }
        
        Checklist saved = checklistRepository.save(checklist);
        return convertToDTO(saved);
    }
    
    @Transactional
    public ChecklistDTO deleteChecklist(Long id) {
        Checklist checklist = getChecklistById(id);
        ChecklistDTO dto = convertToDTO(checklist);
        checklistRepository.deleteById(id);
        return dto;
    }
    
    @Transactional
    public void submitChecklist(Long checklistId) {
        Checklist checklist = getChecklistById(checklistId);
        checklist.setStatus(Checklist.ChecklistStatus.SUBMITTED);
        checklist.setSubmittedAt(LocalDateTime.now());
        checklistRepository.save(checklist);
    }
    
    public long getPendingChecklistsCount() {
        return checklistRepository.countByStatus(Checklist.ChecklistStatus.PENDING);
    }
    
    @Transactional
    public void updateTaskStatus(Long taskId, boolean completed) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setCompleted(completed);
        taskRepository.save(task);
        
        Checklist checklist = task.getChecklist();
        boolean allCompleted = checklist.getTasks().stream()
                .allMatch(Task::isCompleted);
        
        if (allCompleted) {
            checklist.setStatus(Checklist.ChecklistStatus.COMPLETED);
        } else {
            checklist.setStatus(Checklist.ChecklistStatus.IN_PROGRESS);
        }
        checklistRepository.save(checklist);
    }
    
    public List<ChecklistDTO> getChecklistsByStatus(String status) {
        Checklist.ChecklistStatus checklistStatus = Checklist.ChecklistStatus.valueOf(status);
        return checklistRepository.findByStatus(checklistStatus).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<ChecklistDTO> getChecklistsByArea(Long areaId) {
        return checklistRepository.findAll().stream()
                .filter(c -> c.getArea() != null && c.getArea().getId().equals(areaId))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<ChecklistDTO> getChecklistsByEmployee(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return checklistRepository.findByAssignedTo(user).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<ChecklistDTO> getTodayChecklists() {
        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        
        return checklistRepository.findAll().stream()
                .filter(c -> c.getCreatedAt() != null && 
                            c.getCreatedAt().isAfter(startOfDay) && 
                            c.getCreatedAt().isBefore(endOfDay))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<ChecklistDTO> getPendingChecklists() {
        return getChecklistsByStatus("PENDING");
    }
    
    public List<ChecklistDTO> getCompletedChecklists() {
        return getChecklistsByStatus("COMPLETED");
    }
    
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", checklistRepository.count());
        stats.put("pending", checklistRepository.countByStatus(Checklist.ChecklistStatus.PENDING));
        stats.put("completed", checklistRepository.countByStatus(Checklist.ChecklistStatus.COMPLETED));
        stats.put("inProgress", checklistRepository.countByStatus(Checklist.ChecklistStatus.IN_PROGRESS));
        stats.put("submitted", checklistRepository.countByStatus(Checklist.ChecklistStatus.SUBMITTED));
        return stats;
    }
    
    // ADD THIS NEW METHOD
    @Transactional
    public void updateChecklistStatus(Long id, String status) {
        Checklist checklist = getChecklistById(id);
        checklist.setStatus(Checklist.ChecklistStatus.valueOf(status));
        checklist.setUpdatedAt(LocalDateTime.now());
        
        if (status.equals("COMPLETED")) {
            checklist.setSubmittedAt(LocalDateTime.now());
        }
        
        checklistRepository.save(checklist);
    }
    
    private ChecklistDTO convertToDTO(Checklist checklist) {
        ChecklistDTO dto = new ChecklistDTO();
        dto.setId(checklist.getId());
        dto.setName(checklist.getName());
        dto.setStatus(checklist.getStatus().name());
        
        if (checklist.getArea() != null) {
            dto.setAreaId(checklist.getArea().getId());
            dto.setAreaName(checklist.getArea().getName());
        }
        
        if (checklist.getFloor() != null) {
            dto.setFloorId(checklist.getFloor().getId());
            dto.setFloorName(checklist.getFloor().getName());
        }
        
        if (checklist.getAssignedTo() != null) {
            dto.setAssignedToId(checklist.getAssignedTo().getId());
            dto.setAssignedToName(checklist.getAssignedTo().getFullName());
        }
        
        dto.setCreatedAt(checklist.getCreatedAt());
        dto.setUpdatedAt(checklist.getUpdatedAt());
        dto.setSubmittedAt(checklist.getSubmittedAt());
        
        return dto;
    }
}
