package com.hospital.cleaning.controller.api;

import com.hospital.cleaning.dto.ChecklistDTO;
import com.hospital.cleaning.dto.TaskStatusDTO;
import com.hospital.cleaning.service.ChecklistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/checklists")
public class ChecklistRestController {
    
    private final ChecklistService checklistService;
    
    public ChecklistRestController(ChecklistService checklistService) {
        this.checklistService = checklistService;
    }
    
    @GetMapping
    public ResponseEntity<List<ChecklistDTO>> getAllChecklists() {
        return ResponseEntity.ok(checklistService.getAllChecklistDTOs());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ChecklistDTO> getChecklistById(@PathVariable Long id) {
        return ResponseEntity.ok(checklistService.getChecklistDTOById(id));
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ChecklistDTO> createChecklist(@RequestBody ChecklistDTO dto) {
        ChecklistDTO created = checklistService.createChecklist(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ChecklistDTO> updateChecklist(
            @PathVariable Long id, 
            @RequestBody ChecklistDTO dto) {
        ChecklistDTO updated = checklistService.updateChecklist(id, dto);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deleteChecklist(@PathVariable Long id) {
        checklistService.deleteChecklist(id);
        return ResponseEntity.ok(Map.of("message", "Checklist deleted successfully"));
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ChecklistDTO>> getChecklistsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(checklistService.getChecklistsByStatus(status));
    }
    
    @GetMapping("/area/{areaId}")
    public ResponseEntity<List<ChecklistDTO>> getChecklistsByArea(@PathVariable Long areaId) {
        return ResponseEntity.ok(checklistService.getChecklistsByArea(areaId));
    }
    
    @GetMapping("/employee/{userId}")
    public ResponseEntity<List<ChecklistDTO>> getChecklistsByEmployee(@PathVariable Long userId) {
        return ResponseEntity.ok(checklistService.getChecklistsByEmployee(userId));
    }
    
    @PutMapping("/tasks/{taskId}/status")
    public ResponseEntity<Map<String, Object>> updateTaskStatus(
            @PathVariable Long taskId,
            @RequestBody TaskStatusDTO dto) {
        
        checklistService.updateTaskStatus(taskId, dto.isCompleted());
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Task status updated"
        ));
    }
    
    @GetMapping("/today")
    public ResponseEntity<List<ChecklistDTO>> getTodayChecklists() {
        return ResponseEntity.ok(checklistService.getTodayChecklists());
    }
    
    @GetMapping("/pending")
    public ResponseEntity<List<ChecklistDTO>> getPendingChecklists() {
        return ResponseEntity.ok(checklistService.getPendingChecklists());
    }
    
    @GetMapping("/completed")
    public ResponseEntity<List<ChecklistDTO>> getCompletedChecklists() {
        return ResponseEntity.ok(checklistService.getCompletedChecklists());
    }
    
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        return ResponseEntity.ok(checklistService.getStatistics());
    }
}
