package com.hospital.cleaning.dto;

public class EmployeeDTO {
    private Long id;
    private String name;
    private String designation;
    private String shift;
    private Boolean onDuty;

    // Add other fields as required, then generate getters/setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public String getShift() { return shift; }
    public void setShift(String shift) { this.shift = shift; }

    public Boolean getOnDuty() { return onDuty; }
    public void setOnDuty(Boolean onDuty) { this.onDuty = onDuty; }
}
