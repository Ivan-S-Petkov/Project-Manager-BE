package com.example.projectmanager.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.projectmanager.model.Project;
import com.example.projectmanager.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class ProjectController {

  @Autowired
  ProjectRepository projectRepository;

  @GetMapping("/projects")
  public ResponseEntity<List<Project>> getAllProjects(@RequestParam(required = false) String name) {
    try {
      List<Project> projects = new ArrayList<Project>();

      if (name == null)
        projectRepository.findAll().forEach(projects::add);
      else
        projectRepository.findByNameContaining(name).forEach(projects::add);

      if (projects.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(projects, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/projects/{id}")
  public ResponseEntity<Project> getProjectById(@PathVariable("id") long id) {
    Optional<Project> projectData = projectRepository.findById(id);

    if (projectData.isPresent()) {
      return new ResponseEntity<>(projectData.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping("/projects")
  public ResponseEntity<Project> createProject(@RequestBody Project project) {
    try {
      Project _project = projectRepository
          .save(new Project(project.getName(), project.getDescription()));
      return new ResponseEntity<>(_project, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping("/projects/{id}")
  public ResponseEntity<Project> updateProject(@PathVariable("id") long id, @RequestBody Project project) {
    Optional<Project> projectData = projectRepository.findById(id);

    if (projectData.isPresent()) {
      Project _project = projectData.get();
      _project.setName(project.getName());
      _project.setDescription(project.getDescription());
      return new ResponseEntity<>(projectRepository.save(_project), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/projects/{id}")
  public ResponseEntity<HttpStatus> deleteProject(@PathVariable("id") long id) {
    try {
      projectRepository.deleteById(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/projects")
  public ResponseEntity<HttpStatus> deleteAllProjects() {
    try {
      projectRepository.deleteAll();
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}