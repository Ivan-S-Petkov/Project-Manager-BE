package com.example.projectmanager.model;

import org.springframework.beans.factory.annotation.Autowired;

public class Pair {
  @Autowired
  private Project project;
  @Autowired
  private Employee employeeOne;
  @Autowired
  private Employee employeeTwo;

  private long duration;

  public Pair(Project project, Employee employeeOne, Employee employeeTwo, long duration) {
    this.project = project;
    this.employeeOne = employeeOne;
    this.employeeTwo = employeeTwo;
    this.duration = duration;
  }

  public Employee getEmployeeOne() {
    return employeeOne;
  }

  public void setEmployeeOne(Employee employeeOne) {
    this.employeeOne = employeeOne;
  }

  public Employee getEmployeeTwo() {
    return employeeTwo;
  }

  public void setEmployeeTwo(Employee employeeTwo) {
    this.employeeTwo = employeeTwo;
  }

  public long getDuration() {
    return duration;
  }

  public void setDuration(long duration) {
    this.duration = duration;
  }

  public Project getProject() {
    return project;
  }

  public void setProject(Project project) {
    this.project = project;
  }

  @Override
  public String toString() {
    return "Task [ employeeOne=" + employeeOne + ", employeeTwo=" + employeeTwo + ", duration=" + duration + "]";
  }


}