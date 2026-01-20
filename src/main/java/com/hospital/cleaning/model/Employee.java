package com.hospital.cleaning.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @Column(name = "employee_id", unique = true)
    private String employeeId;
    
    private String designation;
    
    private String department;
    
    private String shift; // MORNING, EVENING, NIGHT
    
    private boolean onDuty = false;
    
    @Column(name = "joined_date")
    private LocalDateTime joinedDate;
}
