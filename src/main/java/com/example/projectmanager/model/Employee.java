package com.example.projectmanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "employees")
public class Employee {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "name")
  @NotBlank(message = "Name cannot be blank")
  @Size(min = 2, max = 20, message = "Name cannot be more than 20 characters")
  private String name;

  @Column(name = "department")
  @NotBlank(message = "Department cannot be blank")
  @Size(min = 2, max = 20, message = "Department cannot be more than 20 characters")
  private String department;

  public Employee() {
  }

  public Employee(String name, String department) {
    this.name = name;
    this.department = department;
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }


  @Override
  public String toString() {
    return "Employee [id=" + id + ", name=" + name + ", department=" + department + "]";
  }


}
