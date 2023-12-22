package com.example.projectmanager.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.example.projectmanager.dto.TaskDto;
import com.example.projectmanager.dto.TaskBodyDto;
import com.example.projectmanager.model.Employee;
import com.example.projectmanager.model.Project;
import com.example.projectmanager.model.Task;
import com.example.projectmanager.repository.EmployeeRepository;
import com.example.projectmanager.repository.ProjectRepository;
import com.example.projectmanager.repository.TaskRepository;
import com.example.projectmanager.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class TaskController {

  @Autowired
  TaskRepository taskRepository;

  @Autowired
  EmployeeRepository employeeRepository;

  @Autowired
  ProjectRepository projectRepository;

  @Autowired
  ValidationService validationService;


  @GetMapping("/tasks")
  public ResponseEntity<List<TaskDto>> getAllTasks(@RequestParam(required = false) String employee, String project) {
    try {
      List<TaskDto> tasks = new ArrayList();

      // Get all tasks
      if (employee == null && project == null)
        taskRepository.findAll().forEach(task -> {
          TaskDto taskDto = new TaskDto(task.getId(), task.getEmployee(), task.getProject(), task.getStartDate(), task.getEndDate());
          tasks.add(taskDto);
        });

      // Get all tasks  filter by employeeId parameter
      else if(employee != null) {
        long employeeId = Long.parseLong(employee);
        taskRepository.findByEmployee_Id(employeeId).forEach(task -> {
          TaskDto taskDto = new TaskDto(task.getId(), task.getEmployee(), task.getProject(), task.getStartDate(), task.getEndDate());
          tasks.add(taskDto);
        });
      }

      // Get all tasks filtered by projectId parameter
      else {
        long projectId = Long.parseLong(project);
        taskRepository.findByProject_Id(projectId).forEach(task -> {
          TaskDto taskDto = new TaskDto(task.getId(), task.getEmployee(), task.getProject(), task.getStartDate(), task.getEndDate());
          tasks.add(taskDto);
        });
      }

      if (tasks.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(tasks, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/tasks/{id}")
  public ResponseEntity<Task> getTasksById(@PathVariable("id") long id) {
    Optional<Task> taskData = taskRepository.findById(id);

    if (taskData.isPresent()) {
      return new ResponseEntity<>(taskData.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping("/tasks")
  public ResponseEntity createTask(@RequestBody TaskBodyDto task) {
    try {
      // Check if employee and project exist in DB
      if (!validationService.validateProjectAndEmployee(task.getProject(), task.getEmployee() )) {
        return new ResponseEntity<String>("Employee or Project does not exist!", HttpStatus.NOT_FOUND);
      }

      // Check if startDate and endDate are valid Dates
      if(!validationService.validateStartAndEndDate(task.getStartDate(), task.getEndDate())){
        return new ResponseEntity<>("Invalid Start or End Date!", HttpStatus.NOT_ACCEPTABLE);
      }

      DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/MM/dd");
      Optional<Employee> employee = employeeRepository.findById(task.getEmployee());
      Optional<Project> project = projectRepository.findById(task.getProject());
      LocalDate _startDate = LocalDate.parse(task.getStartDate(), format);
      LocalDate _endDate = Objects.equals(task.getEndDate().toLowerCase(), "null") ? null : LocalDate.parse(task.getEndDate(), format);

      // Check if the employee already is working on this project for this period of time
      if(validationService.alreadyWorkingOnThisProjectAtThisTimeFrame(task.getEmployee(), task.getProject(),_startDate,task.getEndDate())){
        return new ResponseEntity<>("Employee is already working on this project in the specified time frame!", HttpStatus.CONFLICT);
      }

      Task _task = taskRepository.save(new Task(employee.get(), project.get(), _startDate, _endDate));
      return new ResponseEntity<Task>(_task, HttpStatus.CREATED);
    } catch (ResponseStatusException e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/tasks/upload")
  public ResponseEntity createTasks(@RequestBody Map<String,List<Map<String,String>>> data) {
    try {
      List<Task> result = new ArrayList<>();

      for(Map<String,String> task : data.get("data")){

        long employeeId = Long.parseLong(task.get("employee"));
        long projectId = Long.parseLong(task.get("project"));

        // Check if employee and project exist in DB
        if (!validationService.validateProjectAndEmployee(projectId, employeeId )) {
          String error = "Employee: \""+ employeeId + "\" or Project: \""+projectId+"\" does not exist!";
          return new ResponseEntity<String>(error, HttpStatus.NOT_FOUND);
        }

        // Check if startDate and endDate are valid Dates
        if(!validationService.validateStartAndEndDate(task.get("startDate").trim(), task.get("endDate").trim())){
          String error = "Invalid Start: \"" + task.get("startDate").trim() + "\" or End: \"" + task.get("endDate").trim() + "\" !";
          return new ResponseEntity<>(error , HttpStatus.NOT_ACCEPTABLE);
        }

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        Optional<Project> project = projectRepository.findById(projectId);
        LocalDate startDate = LocalDate.parse(task.get("startDate").trim(), format);
        LocalDate endDate = Objects.equals(task.get("endDate").trim(), "null") ? null : LocalDate.parse(task.get("endDate").trim(), format);

        // Check if the employee already is working on this project for this period of time
        if(validationService.alreadyWorkingOnThisProjectAtThisTimeFrame(employeeId, projectId, startDate, task.get("endDate").trim())){
          String error = "Employee is already working on this project in the specified time frame! Record :"+ employeeId + ", " + projectId + ", " + startDate + ", " + endDate;
          return new ResponseEntity<>(error, HttpStatus.CONFLICT);
        }

        Task _task = new Task(employee.get(), project.get(), startDate, endDate);
        result.add(_task);
      }
      
      taskRepository.saveAll(result);
      return new ResponseEntity<>(HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping("/tasks/{id}")
  public ResponseEntity<Task> updateTask(@PathVariable("id") long id, @RequestBody Task task) {
    Optional<Task> taskData = taskRepository.findById(id);

    if (taskData.isPresent()) {
      Task _task = taskData.get();
      _task.setEmployee(task.getEmployee());
      _task.setProject(task.getProject());
      _task.setStartDate(task.getStartDate());
      _task.setEndDate(task.getEndDate());
      return new ResponseEntity<>(taskRepository.save(_task), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/tasks/{id}")
  public ResponseEntity<HttpStatus> deleteTask(@PathVariable("id") long id) {
    try {
      taskRepository.deleteById(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/tasks")
  public ResponseEntity<HttpStatus> deleteAllTasks() {
    try {
      taskRepository.deleteAll();
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}