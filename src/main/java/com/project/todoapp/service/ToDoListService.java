package com.project.todoapp.service;

import com.project.todoapp.model.Task;
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

    public List<Task> getToDoList() {
        List<Task> toDoList = new ArrayList<>();
        toDoListRepository.findAll().forEach(task -> {
            toDoList.add(task);
        });
        return toDoList;
    }

    public Task getTask(long id) {
        return toDoListRepository.findById(id).orElse(null);
    }

    public void deleteTask(long id) {
        toDoListRepository.deleteById(id);
    }

    public void addTask(Task newTask) {
        toDoListRepository.save(newTask);
    }

    public void updateTask(Task updatedTask, UUID id) {
        toDoListRepository.save(updatedTask);
    }

}
