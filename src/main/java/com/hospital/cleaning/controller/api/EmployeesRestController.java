package com.hospital.cleaning.controller.api;

import com.hospital.cleaning.dto.EmployeeDTO;
import com.hospital.cleaning.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "*", maxAge = 3600)
public class EmployeesRestController {
    @Autowired
    private EmployeeService employeeService;

    /** Get all employees */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        List<EmployeeDTO> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    /** Get employee by ID */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id) {
        EmployeeDTO employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    /** Create new employee */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<EmployeeDTO> createEmployee(
            @Valid @RequestBody EmployeeDTO employeeDTO) {
        EmployeeDTO created = employeeService.createEmployee(employeeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /** Update employee */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<EmployeeDTO> updateEmployee(
            @PathVariable Long id, @Valid @RequestBody EmployeeDTO employeeDTO) {
        EmployeeDTO updated = employeeService.updateEmployee(id, employeeDTO);
        return ResponseEntity.ok(updated);
    }

    /** Delete employee */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok(Map.of("message", "Employee deleted successfully"));
    }

    /** Get employees on duty */
    @GetMapping("/on-duty")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<EmployeeDTO>> getOnDutyEmployees() {
        List<EmployeeDTO> employees = employeeService.getOnDutyEmployees();
        return ResponseEntity.ok(employees);
    }

    /** Get employees by shift */
    @GetMapping("/shift/{shift}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByShift(
            @PathVariable String shift) {
        List<EmployeeDTO> employees = employeeService.getEmployeesByShift(shift);
        return ResponseEntity.ok(employees);
    }

    /** Mark employee on duty */
    @PutMapping("/{id}/on-duty")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<EmployeeDTO> markOnDuty(@PathVariable Long id) {
        EmployeeDTO employee = employeeService.markOnDuty(id, true);
        return ResponseEntity.ok(employee);
    }

    /** Mark employee off duty */
    @PutMapping("/{id}/off-duty")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<EmployeeDTO> markOffDuty(@PathVariable Long id) {
        EmployeeDTO employee = employeeService.markOnDuty(id, false);
        return ResponseEntity.ok(employee);
    }
}
