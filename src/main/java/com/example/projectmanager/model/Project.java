package com.example.projectmanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "projects")
public class Project {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "name")
  @NotBlank(message = "Project name cannot be blank")
  @Size(min = 2, max = 20, message = "Project name cannot be more than 20 characters")
  private String name;

  @Column(name = "description")
  @NotBlank(message = "Description cannot be blank")
  @Size(min = 10, max = 200, message = "Description cannot be more than 20 characters")
  private String description;


  public Project() {
  }

  public Project(String name, String description) {
    this.name = name;
    this.description = description;
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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  @Override
  public String toString() {
    return "Project [id=" + id + ", name=" + name + ", description=" + description + "]";
  }

}
