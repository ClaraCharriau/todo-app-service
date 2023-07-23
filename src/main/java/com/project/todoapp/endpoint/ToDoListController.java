package com.project.todoapp.endpoint;

import com.project.todoapp.dto.TaskDto;
import com.project.todoapp.exception.TaskAlreadyExistException;
import com.project.todoapp.exception.TaskNotFoundException;
import com.project.todoapp.exception.ZeroTaskFoundException;
import com.project.todoapp.service.ToDoListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public List<TaskDto> getTodos() throws ZeroTaskFoundException {
        return toDoListService.getToDoList();
    }

    @GetMapping("/{id}")
    public TaskDto getTask(@PathVariable("id") UUID id) throws TaskNotFoundException {
        return toDoListService.getTask(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable("id") UUID id) throws TaskNotFoundException {
        toDoListService.deleteTask(id);
    }

    @PostMapping()
    @ResponseStatus(value = HttpStatus.CREATED)
    public void createTask(@RequestBody TaskDto newTaskDto) throws TaskAlreadyExistException {
        toDoListService.createTask(newTaskDto);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void updateTask(@RequestBody TaskDto updatedTaskDto, @PathVariable UUID id) throws TaskNotFoundException {
        toDoListService.updateTask(updatedTaskDto, id);
    }

}
