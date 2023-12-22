package com.example.projectmanager.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "tasks")
public class Task {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @ManyToOne
  @JoinColumn(name = "employee_id")
  private Employee employee;

  @ManyToOne
  @JoinColumn(name = "project_id")
  private Project project;
  @Column(name = "startDate")
  @Temporal(TemporalType.DATE)
  private LocalDate startDate;

  @Column(name = "endDate")
  @Temporal(TemporalType.DATE)
  private LocalDate endDate;


  public Task() {
  }

  public Task(Employee employee, Project project, LocalDate startDate, LocalDate endDate) {
    this.employee = employee;
    this.project = project;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public long getId() {
    return id;
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


@Override
public String toString() {
  return "Task [id=" + id + ", employee=" + employee + ", project=" + project + ", startDate=" + startDate + ", endDate=" + endDate + "]";
}


}
