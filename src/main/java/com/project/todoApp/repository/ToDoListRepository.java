package com.project.todoApp.repository;

import com.project.todoApp.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToDoListRepository extends JpaRepository<Task, Long> {
}
