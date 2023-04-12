package com.project.todoapp.endpoint;

import com.project.todoapp.exception.TaskNotFoundException;
import com.project.todoapp.model.TaskEntity;
import com.project.todoapp.service.ToDoListService;
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
    public List<TaskEntity> getTodos() {
        return toDoListService.getToDoList();
    }

    @GetMapping("{id}")
    public TaskEntity getTask(@PathVariable("id") long id) throws TaskNotFoundException {
        return toDoListService.getTask(id);
    }

    @DeleteMapping("{id}")
    public void deleteTask(@PathVariable("id") long id) throws TaskNotFoundException {
        toDoListService.deleteTask(id);
    }

    @PostMapping()
    public void addTask(@RequestBody TaskEntity newTaskEntity) {
        toDoListService.addTask(newTaskEntity);
    }

    @PatchMapping("{id}")
    public void updateTask(@RequestBody TaskEntity updatedTaskEntity, @PathVariable UUID id) throws TaskNotFoundException {
        toDoListService.updateTask(updatedTaskEntity, id);
    }

}
