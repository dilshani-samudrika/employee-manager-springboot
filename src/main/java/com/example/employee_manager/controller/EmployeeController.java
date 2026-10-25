
package com.example.employee_manager.controller;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.employee_manager.model.Employee;
import com.example.employee_manager.service.EmployeeService;

@Controller
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/employees";
    }

    @GetMapping("/employees")
    public String listEmployees(
            @RequestParam(required = false) String keyword,
            Model model) {

        model.addAttribute(
            "employees",
            employeeService.searchEmployees(keyword)
        );

        model.addAttribute("employee", new Employee());
        model.addAttribute("keyword", keyword);

        return "employee";
    }

    @PostMapping("/employees")
    public String saveEmployee(
            @Valid @ModelAttribute("employee") Employee employee,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("employees",
                employeeService.getAllEmployees());
            return "employee";
        }

        if (employeeService.emailExists(employee.getEmail())) {
            result.rejectValue(
                "email",
                "duplicate",
                "This email is already registered"
            );

            model.addAttribute("employees",
                employeeService.getAllEmployees());

            return "employee";
        }

        employeeService.saveEmployee(employee);

        redirectAttributes.addFlashAttribute(
            "successMessage", "Employee saved successfully!"
        );

        return "redirect:/employees";
    }

    @GetMapping("/employees/{id}/edit")
    public String editEmployee(
            @PathVariable Long id, Model model) {

        model.addAttribute(
            "employee", employeeService.getEmployeeById(id)
        );

        return "edit-employee";
    }

    @PostMapping("/employees/{id}/update")
    public String updateEmployee(
            @PathVariable Long id,
            @Valid @ModelAttribute("employee") Employee employee,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        Employee existing = employeeService.getEmployeeById(id);

        if (result.hasErrors()) {
            return "edit-employee";
        }

        if (employeeService.emailExistsForAnotherEmployee(
                employee.getEmail(), id)) {

            result.rejectValue(
                "email", "duplicate",
                "This email is already registered"
            );

            return "edit-employee";
        }

        employeeService.updateEmployee(id, employee);

        redirectAttributes.addFlashAttribute(
            "successMessage", "Employee updated successfully!"
        );

        return "redirect:/employees";
    }

    @PostMapping("/employees/{id}/delete")
    public String deleteEmployee(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        employeeService.deleteEmployee(id);

        redirectAttributes.addFlashAttribute(
            "successMessage", "Employee deleted successfully!"
        );

        return "redirect:/employees";
    }
}
