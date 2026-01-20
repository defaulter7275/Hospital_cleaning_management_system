package com.hospital.cleaning.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "floors")
public class Floor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(name = "name_hindi")
    private String nameHindi;
    
    @Column(name = "floor_number")
    private Integer floorNumber;
    
    @OneToMany(mappedBy = "floor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Area> areas = new ArrayList<>();
    
    // Constructors
    public Floor() {}
    
    public Floor(String name, String nameHindi, Integer floorNumber) {
        this.name = name;
        this.nameHindi = nameHindi;
        this.floorNumber = floorNumber;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getNameHindi() { return nameHindi; }
    public void setNameHindi(String nameHindi) { this.nameHindi = nameHindi; }
    
    public Integer getFloorNumber() { return floorNumber; }
    public void setFloorNumber(Integer floorNumber) { this.floorNumber = floorNumber; }
    
    public List<Area> getAreas() { return areas; }
    public void setAreas(List<Area> areas) { this.areas = areas; }
}
