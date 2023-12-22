package com.example.projectmanager.dto;

public class TaskBodyDto {
  private long employee;

  private long project;
  private String startDate;

  private String endDate;

  public TaskBodyDto(long employee, long project, String startDate, String endDate) {

    this.employee = employee;
    this.project = project;
    this.startDate = startDate;
    this.endDate = endDate ;
  }

  public long getEmployee() {
    return employee;
  }

  public void setEmployee(long employee) {
    this.employee = employee;
  }

  public long getProject() {
    return project;
  }

  public void setProject(long project) {
    this.project = project;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  @Override
  public String toString() {
    return "Task [ employee=" + employee + ", project=" + project + ", startDate=" + startDate + ", endDate=" + endDate + "]";
  }

}
