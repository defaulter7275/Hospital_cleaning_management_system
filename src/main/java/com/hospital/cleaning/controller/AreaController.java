package com.hospital.cleaning.controller;

import com.hospital.cleaning.model.Area;
import com.hospital.cleaning.model.Floor;
import com.hospital.cleaning.repository.AreaRepository;
import com.hospital.cleaning.repository.FloorRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/areas")
public class AreaController {
    
    private final AreaRepository areaRepository;
    private final FloorRepository floorRepository;
    
    public AreaController(AreaRepository areaRepository, FloorRepository floorRepository) {
        this.areaRepository = areaRepository;
        this.floorRepository = floorRepository;
    }
    
    // List all areas
    @GetMapping
    public String listAreas(Model model) {
        model.addAttribute("areas", areaRepository.findAll());
        return "areas/list";
    }
    
    // Show create area form - ADMIN only
    @GetMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String showCreateForm(Model model) {
        model.addAttribute("area", new Area());
        model.addAttribute("floors", floorRepository.findAll());
        return "areas/create";
    }
    
    // Create new area - ADMIN only
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String createArea(@ModelAttribute Area area, 
                            @RequestParam Long floorId,
                            RedirectAttributes redirectAttributes) {
        try {
            Floor floor = floorRepository.findById(floorId)
                    .orElseThrow(() -> new RuntimeException("Floor not found"));
            area.setFloor(floor);
            areaRepository.save(area);
            redirectAttributes.addFlashAttribute("success", "Area created successfully!");
            return "redirect:/areas";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create area: " + e.getMessage());
            return "redirect:/areas/create";
        }
    }
    
    // Show edit area form - ADMIN only
    @GetMapping("/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditForm(@PathVariable Long id, Model model) {
        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Area not found"));
        model.addAttribute("area", area);
        model.addAttribute("floors", floorRepository.findAll());
        return "areas/edit";
    }
    
    // Update area - ADMIN only
    @PostMapping("/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateArea(@PathVariable Long id, 
                            @ModelAttribute Area area,
                            @RequestParam Long floorId,
                            RedirectAttributes redirectAttributes) {
        try {
            Area existingArea = areaRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Area not found"));
            
            Floor floor = floorRepository.findById(floorId)
                    .orElseThrow(() -> new RuntimeException("Floor not found"));
            
            existingArea.setName(area.getName());
            existingArea.setNameHindi(area.getNameHindi());
            existingArea.setDescription(area.getDescription());
            existingArea.setFloor(floor);
            
            areaRepository.save(existingArea);
            redirectAttributes.addFlashAttribute("success", "Area updated successfully!");
            return "redirect:/areas";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update area: " + e.getMessage());
            return "redirect:/areas/" + id + "/edit";
        }
    }
    
    // Delete area - ADMIN only
    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteArea(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            areaRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Area deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete area: " + e.getMessage());
        }
        return "redirect:/areas";
    }
}
