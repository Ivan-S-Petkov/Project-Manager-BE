package com.example.projectmanager.repository;


import com.example.projectmanager.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
  List<Task> findByEmployee_Id(long id);
  List<Task> findByProject_Id(long id);
}