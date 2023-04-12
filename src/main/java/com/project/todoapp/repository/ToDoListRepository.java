package com.project.todoapp.repository;

import com.project.todoapp.model.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ToDoListRepository extends JpaRepository<TaskEntity, UUID> {
}
