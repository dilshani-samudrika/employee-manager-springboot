
package com.example.employee_manager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.employee_manager.model.Employee;

public interface EmployeeRepository
        extends JpaRepository<Employee, Long> {

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(
        String email, Long id
    );

    Optional<Employee> findByEmailIgnoreCase(String email);

    java.util.List<Employee> findByFullNameContainingIgnoreCase(
        String keyword
    );
}