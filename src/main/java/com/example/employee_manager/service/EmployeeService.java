
package com.example.employee_manager.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.employee_manager.model.Employee;
import com.example.employee_manager.repository.EmployeeRepository;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
            .orElseThrow(() ->
                new IllegalArgumentException("Employee not found"));
    }

    public boolean emailExists(String email) {
        return employeeRepository.existsByEmailIgnoreCase(email);
    }

    public boolean emailExistsForAnotherEmployee(
            String email, Long id) {
        return employeeRepository
            .existsByEmailIgnoreCaseAndIdNot(email, id);
    }

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Long id, Employee formData) {
        Employee existing = getEmployeeById(id);

        existing.setFullName(formData.getFullName());
        existing.setEmail(formData.getEmail());
        existing.setPhone(formData.getPhone());
        existing.setDepartment(formData.getDepartment());
        existing.setSalary(formData.getSalary());

        return employeeRepository.save(existing);
    }

    public void deleteEmployee(Long id) {
        Employee employee = getEmployeeById(id);
        employeeRepository.delete(employee);
    }

    public List<Employee> searchEmployees(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return getAllEmployees();
        }

        return employeeRepository
            .findByFullNameContainingIgnoreCase(keyword.trim());
    }
}
