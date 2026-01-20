package com.hospital.cleaning.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tasks")
public class Task {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String description;
    
    @ManyToOne
    @JoinColumn(name = "checklist_id", nullable = false)
    private Checklist checklist;
    
    @Column(nullable = false)
    private boolean completed = false;
    
    @Column(name = "display_order")
    private Integer displayOrder;
    
    // Constructors
    public Task() {}
    
    public Task(String description, Checklist checklist, boolean completed, Integer displayOrder) {
        this.description = description;
        this.checklist = checklist;
        this.completed = completed;
        this.displayOrder = displayOrder;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Checklist getChecklist() {
        return checklist;
    }
    
    public void setChecklist(Checklist checklist) {
        this.checklist = checklist;
    }
    
    public boolean isCompleted() {
        return completed;
    }
    
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    
    public Integer getDisplayOrder() {
        return displayOrder;
    }
    
    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
}
