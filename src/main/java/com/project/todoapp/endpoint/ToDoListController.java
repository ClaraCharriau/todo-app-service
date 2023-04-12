package com.project.todoapp.endpoint;

import com.project.todoapp.dto.TaskDto;
import com.project.todoapp.exception.TaskNotFoundException;
import com.project.todoapp.exception.ZeroTaskFoundException;
import com.project.todoapp.service.ToDoListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<TaskDto>> getTodos() throws ZeroTaskFoundException {
        try {
            return toDoListService.getToDoList();
        } catch (ZeroTaskFoundException e) {
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTask(@PathVariable("id") UUID id) throws TaskNotFoundException {

        try {
            return toDoListService.getTask(id);
        } catch (TaskNotFoundException e) {
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable("id") UUID id) throws TaskNotFoundException {
        try {
            toDoListService.deleteTask(id);
        } catch (TaskNotFoundException e) {
            throw e;
        }
    }

    @PostMapping()
    public void addTask(@RequestBody TaskDto newTaskDto) {
        toDoListService.addTask(newTaskDto);
    }

    @PatchMapping("/{id}")
    public void updateTask(@RequestBody TaskDto updatedTaskDto, @PathVariable UUID id) throws TaskNotFoundException {
        toDoListService.updateTask(updatedTaskDto, id);
    }

}
