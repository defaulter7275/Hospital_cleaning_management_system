package com.hospital.cleaning.dto;

public class TaskStatusDTO {
    private boolean completed;
    
    // Constructors
    public TaskStatusDTO() {}
    
    public TaskStatusDTO(boolean completed) {
        this.completed = completed;
    }
    
    // Getters and Setters
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}
