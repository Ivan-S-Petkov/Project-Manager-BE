package com.example.projectmanager.dto;

import com.example.projectmanager.model.Employee;
import com.example.projectmanager.model.Project;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


public class TaskDto {

  private long id;

  private Employee employee;

  private Project project;
  private LocalDate startDate;

  private LocalDate endDate;

  private long duration;

  public TaskDto(long id, Employee employee, Project project, LocalDate startDate, LocalDate endDate) {
    this.id = id;
    this.employee = employee;
    this.project = project;
    this.startDate = startDate;
    this.endDate = endDate ;
    this.duration = ChronoUnit.DAYS.between(this.startDate, this.endDate != null ? this.endDate : LocalDate.now())+1;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Employee getEmployee() {
    return employee;
  }

  public void setEmployee(Employee employee) {
    this.employee = employee;
  }

  public Project getProject() {
    return project;
  }

  public void setProject(Project project) {
    this.project = project;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  public long getDuration() {
    return duration;
  }

}

