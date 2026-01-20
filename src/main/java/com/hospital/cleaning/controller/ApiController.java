package com.hospital.cleaning.controller;

import com.hospital.cleaning.dto.TaskStatusDTO;
import com.hospital.cleaning.service.ChecklistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {
    
    private final ChecklistService checklistService;
    
    public ApiController(ChecklistService checklistService) {
        this.checklistService = checklistService;
    }
    
    @PutMapping("/tasks/{taskId}/status")
    public ResponseEntity<?> updateTaskStatus(
            @PathVariable Long taskId,
            @RequestBody TaskStatusDTO dto) {
        
        checklistService.updateTaskStatus(taskId, dto.isCompleted());
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Task updated successfully"
        ));
    }
    
    @PostMapping("/checklists/{checklistId}/submit")
    public ResponseEntity<?> submitChecklist(@PathVariable Long checklistId) {
        checklistService.submitChecklist(checklistId);
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Checklist submitted successfully"
        ));
    }
}
