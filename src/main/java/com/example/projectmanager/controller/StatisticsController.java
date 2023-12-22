package com.example.projectmanager.controller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;


import com.example.projectmanager.model.Pair;
import com.example.projectmanager.model.Task;
import com.example.projectmanager.repository.EmployeeRepository;
import com.example.projectmanager.repository.ProjectRepository;
import com.example.projectmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class StatisticsController {

  @Autowired
  TaskRepository taskRepository;

  @Autowired
  EmployeeRepository employeeRepository;

  @Autowired
  ProjectRepository projectRepository;

  @GetMapping("/statistics")
  public ResponseEntity<List<Pair>> getAllStatistics() {
    try {

      // Get list of all tasks sorted by Project ID, then Start Date and last End Date.
      List<Task> sortedTasks = taskRepository.findAll(Sort.by("project_id").ascending().and(Sort.by("startDate").ascending().and(Sort.by("endDate").descending())));
      List<Pair> workingPairs = new ArrayList<>();

      for (int i = 0; i < sortedTasks.size()-1; i++) {
        // Set endDate to today in case it is null
        LocalDate initialEndDate = sortedTasks.get(i).getEndDate() != null ? sortedTasks.get(i).getEndDate() : LocalDate.now();

        for (int j = i+1; j < sortedTasks.size() ; j++) {

          // Check if next project_Id is different from the initial one
          if(sortedTasks.get(i).getProject().getId() != sortedTasks.get(j).getProject().getId()) {
            break;
          }

          // Check if initial endDate is before next startDate
          if(initialEndDate.isBefore(sortedTasks.get(j).getStartDate())){
            break;
          }

          // Set endDate to today in case it is null
          LocalDate currentEndDate = sortedTasks.get(j).getEndDate() != null ? sortedTasks.get(j).getEndDate() : LocalDate.now();

          long duration = 0;
          // Check if next endDate is before initial endDate
          if(currentEndDate.isBefore(initialEndDate)){
            duration = ChronoUnit.DAYS.between(sortedTasks.get(j).getStartDate(), currentEndDate) + 1;
          }
          else {
            duration = ChronoUnit.DAYS.between(sortedTasks.get(j).getStartDate(), initialEndDate) + 1;
          }

          // Create new pair of colleagues who worked together
          Pair pair = new Pair(sortedTasks.get(i).getProject(), sortedTasks.get(i).getEmployee(), sortedTasks.get(j).getEmployee(), duration);
          workingPairs.add(pair);
        }
      }

      return new ResponseEntity<>(workingPairs, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

//  @GetMapping("/tasks/{id}")
//  public ResponseEntity<Task> getTasksById(@PathVariable("id") long id) {
//    Optional<Task> taskData = taskRepository.findById(id);
//
//    if (taskData.isPresent()) {
//      return new ResponseEntity<>(taskData.get(), HttpStatus.OK);
//    } else {
//      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }
//  }
//
//  @PostMapping("/tasks")
//  public ResponseEntity<Task> createTask(@RequestBody Map<String,String> data) {
//    try {
////      Task task = data;
////      taskRepository.save(task);
//      return new ResponseEntity<>(HttpStatus.CREATED);
//    } catch (Exception e) {
//      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//  }
//
//  @PostMapping("/tasks/upload")
//  public ResponseEntity<Task> createTasks(@RequestBody Map<String,List<Map<String,String>>> data) {
//    try {
//      List<Task> result = new ArrayList<>();
//
//      for(Map<String,String> task : data.get("data")){
//        long employeeId = Long.parseLong(task.get("employeeId"));
//        long projectId = Long.parseLong(task.get("projectId"));
//
//        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/MM/dd");
//
//        LocalDate startDate = LocalDate.parse(task.get("startDate").trim(), format);
//        LocalDate endDate = null;
//
//        if(!Objects.equals(task.get("endDate").trim().toLowerCase(), "null")){
//          endDate = LocalDate.parse(task.get("endDate").trim(), format);
//        }
//        Employee employee = employeeRepository.getReferenceById(employeeId);
//        Project project = projectRepository.getReferenceById(projectId);
//        Task newTask = new Task(employee,project,startDate,endDate);
//
//        result.add(newTask);
//      }
//
//      taskRepository.saveAll(result );
//      return new ResponseEntity<>(HttpStatus.CREATED);
//    } catch (Exception e) {
//      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//  }
//
//  @PutMapping("/tasks/{id}")
//  public ResponseEntity<Task> updateTask(@PathVariable("id") long id, @RequestBody Task task) {
//    Optional<Task> taskData = taskRepository.findById(id);
//
//    if (taskData.isPresent()) {
//      Task _task = taskData.get();
//      _task.setEmployee(task.getEmployee());
//      _task.setProject(task.getProject());
//      _task.setStartDate(task.getStartDate());
//      _task.setEndDate(task.getEndDate());
//      return new ResponseEntity<>(taskRepository.save(_task), HttpStatus.OK);
//    } else {
//      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }
//  }
//
//  @DeleteMapping("/tasks/{id}")
//  public ResponseEntity<HttpStatus> deleteTask(@PathVariable("id") long id) {
//    try {
//      taskRepository.deleteById(id);
//      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    } catch (Exception e) {
//      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//  }
//
//  @DeleteMapping("/tasks")
//  public ResponseEntity<HttpStatus> deleteAllTasks() {
//    try {
//      taskRepository.deleteAll();
//      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    } catch (Exception e) {
//      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//  }

}