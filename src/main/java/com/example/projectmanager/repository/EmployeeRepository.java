package com.example.projectmanager.repository;

import com.example.projectmanager.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
  List<Employee> findByNameContaining(String name);

}