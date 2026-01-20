package com.hospital.cleaning.controller;

import com.hospital.cleaning.model.Floor;
import com.hospital.cleaning.model.Area;
import com.hospital.cleaning.repository.FloorRepository;
import com.hospital.cleaning.repository.AreaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/floors")
public class FloorController {
    
    private final FloorRepository floorRepository;
    private final AreaRepository areaRepository;
    
    public FloorController(FloorRepository floorRepository, AreaRepository areaRepository) {
        this.floorRepository = floorRepository;
        this.areaRepository = areaRepository;
    }
    
    // List all floors
    @GetMapping
    public String listFloors(Model model) {
        List<Floor> floors = floorRepository.findAll();
        model.addAttribute("floors", floors);
        return "floors/list";
    }
    
    // View single floor with all areas
    @GetMapping("/{id}")
    public String viewFloor(@PathVariable Long id, Model model) {
        Floor floor = floorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Floor not found"));
        
        List<Area> areas = areaRepository.findByFloor(floor);
        
        model.addAttribute("floor", floor);
        model.addAttribute("areas", areas);
        return "floors/view";
    }
    
    // Show create floor form - ADMIN only
    @GetMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String showCreateForm(Model model) {
        model.addAttribute("floor", new Floor());
        return "floors/create";
    }
    
    // Create new floor - ADMIN only
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String createFloor(@ModelAttribute Floor floor, RedirectAttributes redirectAttributes) {
        try {
            floorRepository.save(floor);
            redirectAttributes.addFlashAttribute("success", "Floor created successfully!");
            return "redirect:/floors";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create floor: " + e.getMessage());
            return "redirect:/floors/create";
        }
    }
    
    // Show edit floor form - ADMIN only
    @GetMapping("/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditForm(@PathVariable Long id, Model model) {
        Floor floor = floorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Floor not found"));
        model.addAttribute("floor", floor);
        return "floors/edit";
    }
    
    // Update floor - ADMIN only
    @PostMapping("/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateFloor(@PathVariable Long id, @ModelAttribute Floor floor,
                             RedirectAttributes redirectAttributes) {
        try {
            Floor existingFloor = floorRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Floor not found"));
            
            existingFloor.setName(floor.getName());
            existingFloor.setNameHindi(floor.getNameHindi());
            existingFloor.setFloorNumber(floor.getFloorNumber());
            
            floorRepository.save(existingFloor);
            redirectAttributes.addFlashAttribute("success", "Floor updated successfully!");
            return "redirect:/floors";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update floor: " + e.getMessage());
            return "redirect:/floors/" + id + "/edit";
        }
    }
    
    // Delete floor - ADMIN only
    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteFloor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            floorRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Floor deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete floor: " + e.getMessage());
        }
        return "redirect:/floors";
    }
    
    // Get all areas of a specific floor
    @GetMapping("/{id}/areas")
    public String getFloorAreas(@PathVariable Long id, Model model) {
        Floor floor = floorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Floor not found"));
        
        List<Area> areas = areaRepository.findByFloor(floor);
        
        model.addAttribute("floor", floor);
        model.addAttribute("areas", areas);
        return "floors/areas";
    }
}
