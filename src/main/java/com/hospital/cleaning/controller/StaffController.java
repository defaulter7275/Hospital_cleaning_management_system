package com.hospital.cleaning.controller;

import com.hospital.cleaning.model.User;
import com.hospital.cleaning.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/staff")
public class StaffController {
    
    private final UserService userService;
    
    public StaffController(UserService userService) {
        this.userService = userService;
    }
    
    // View staff list - accessible by ADMIN and MANAGER
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String listStaff(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "staff/list";
    }
    
    // Add new staff - ADMIN ONLY
    @GetMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String showAddForm(Model model) {
        model.addAttribute("user", new User());
        return "staff/add";
    }
    
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String addStaff(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            userService.createUser(user);
            redirectAttributes.addFlashAttribute("success", "User created successfully!");
            return "redirect:/staff";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create user: " + e.getMessage());
            return "redirect:/staff/add";
        }
    }
    
    // Edit staff - ADMIN ONLY
    @GetMapping("/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "staff/edit";
    }
    
    @PostMapping("/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateStaff(@PathVariable Long id, @ModelAttribute User user, 
                             RedirectAttributes redirectAttributes) {
        try {
            userService.updateUser(id, user);
            redirectAttributes.addFlashAttribute("success", "User updated successfully!");
            return "redirect:/staff";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update user: " + e.getMessage());
            return "redirect:/staff/" + id + "/edit";
        }
    }
    
    // Delete staff - ADMIN ONLY
    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteStaff(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("success", "User deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete user: " + e.getMessage());
        }
        return "redirect:/staff";
    }
}
