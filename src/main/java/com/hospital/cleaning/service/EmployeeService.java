package com.hospital.cleaning.service;

import com.hospital.cleaning.dto.EmployeeDTO;
import java.util.List;

public interface EmployeeService {
    List<EmployeeDTO> getAllEmployees();
    EmployeeDTO getEmployeeById(Long id);
    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);
    EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO);
    void deleteEmployee(Long id);
    List<EmployeeDTO> getOnDutyEmployees();
    List<EmployeeDTO> getEmployeesByShift(String shift);
    EmployeeDTO markOnDuty(Long id, boolean onDuty);
}
