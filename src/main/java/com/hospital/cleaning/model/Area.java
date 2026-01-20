package com.hospital.cleaning.model;

import jakarta.persistence.*;

@Entity
@Table(name = "areas")
public class Area {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(name = "name_hindi")
    private String nameHindi;
    
    @ManyToOne
    @JoinColumn(name = "floor_id", nullable = false)
    private Floor floor;
    
    private String description;
    
    // Constructors
    public Area() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getNameHindi() { return nameHindi; }
    public void setNameHindi(String nameHindi) { this.nameHindi = nameHindi; }
    
    public Floor getFloor() { return floor; }
    public void setFloor(Floor floor) { this.floor = floor; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
