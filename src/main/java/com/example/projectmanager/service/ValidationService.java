package com.example.projectmanager.service;

import com.example.projectmanager.model.Task;
import com.example.projectmanager.repository.EmployeeRepository;
import com.example.projectmanager.repository.ProjectRepository;
import com.example.projectmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ValidationService {

  @Autowired
  public  EmployeeRepository employeeRepository;

  @Autowired
  public  ProjectRepository projectRepository;

  @Autowired
  public  TaskRepository taskRepository;

  public  DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy/MM/dd");

  private  boolean projectExists(long id){
    return projectRepository.findById(id).isPresent();
  }

  private  boolean employeeExists(long id){
    return  employeeRepository.findById(id).isPresent();
  }

  public  boolean validateProjectAndEmployee(long projectId, long employeeId){
    return projectExists(projectId) && employeeExists(employeeId);
  }

  public  boolean validateStartAndEndDate(String startDate, String endDate){
    try {
      LocalDate.parse(startDate, DATE_FORMAT);
      if (!Objects.equals(endDate.toLowerCase(), "null") ) {
        LocalDate.parse(endDate, DATE_FORMAT);
      }
      return true;
    } catch (DateTimeParseException e) {
      return false;
    }
  }

  public boolean alreadyWorkingOnThisProjectAtThisTimeFrame(long employeeId, long projectId, LocalDate _startDate, String endDate ){

    // Assign today's date to endDate if "null"
    LocalDate _endDate ;
    if (!Objects.equals(endDate.toLowerCase(), "null") ) {
      _endDate = LocalDate.parse(endDate, DATE_FORMAT);
    } else {
      _endDate = LocalDate.now();
    }

    // Get all employee tasks from DB where working on the same projectId
    List<Task> _employeeTasks  = new ArrayList<Task>();
    taskRepository.findByEmployee_Id(employeeId).forEach(task -> {
      if(task.getProject().getId() == projectId){
        Task _t = new Task(task.getEmployee(), task.getProject(),task.getStartDate(),task.getEndDate());
        _employeeTasks.add(_t);
      }
    });

    // Loop through the employee tasks and check if the startDate is after task_EndDate,
    for (Task task : _employeeTasks){
      LocalDate _taskEndDate = task.getEndDate()!= null? task.getEndDate() : LocalDate.now();
      if(!(_startDate.isAfter(_taskEndDate) || _endDate.isBefore(task.getStartDate()))){
        return true;
      }
    }
    return false;
  }

}
