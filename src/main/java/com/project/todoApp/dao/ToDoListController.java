package com.project.todoApp.dao;

import com.project.todoApp.model.Task;
import com.project.todoApp.service.ToDoListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/todolist")
public class ToDoListController {

    @Autowired
    private ToDoListService toDoListService;

    @GetMapping
    public List<Task> getTodos() {
        return toDoListService.getToDoList();
    }

    @GetMapping("{id}")
    public Task getTask(@PathVariable long id) {
        return toDoListService.getTask(id);
    }

    @DeleteMapping("{id}")
    public void deleteTask(@PathVariable long id) {
        toDoListService.deleteTask(id);
    }

    @PostMapping()
    public void addTask(@RequestBody Task newTask) {
        toDoListService.addTask(newTask);
    }

    @PatchMapping("{id}")
    public void updateTask(@RequestBody Task updatedTask, @PathVariable UUID id) {
        toDoListService.updateTask(updatedTask, id);
    }

}
