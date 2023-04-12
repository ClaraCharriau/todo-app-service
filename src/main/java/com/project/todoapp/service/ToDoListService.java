package com.project.todoapp.service;

import com.project.todoapp.model.TaskEntity;
import com.project.todoapp.repository.ToDoListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ToDoListService {

    @Autowired
    private ToDoListRepository toDoListRepository;

    public List<TaskEntity> getToDoList() {
        List<TaskEntity> toDoList = new ArrayList<>();
        toDoListRepository.findAll().forEach(taskEntity -> {
            toDoList.add(taskEntity);
        });
        return toDoList;
    }

    public TaskEntity getTask(long id) {
        return toDoListRepository.findById(id).orElse(null);
    }

    public void deleteTask(long id) {
        toDoListRepository.deleteById(id);
    }

    public void addTask(TaskEntity newTaskEntity) {
        toDoListRepository.save(newTaskEntity);
    }

    public void updateTask(TaskEntity updatedTaskEntity, UUID id) {
        toDoListRepository.save(updatedTaskEntity);
    }

}
