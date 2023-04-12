package com.project.todoapp.service;

import com.project.todoapp.dto.TaskDto;
import com.project.todoapp.exception.TaskNotFoundException;
import com.project.todoapp.exception.ZeroTaskFoundException;
import com.project.todoapp.mapper.TaskMapper;
import com.project.todoapp.model.TaskEntity;
import com.project.todoapp.repository.ToDoListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ToDoListService {

    @Autowired
    private ToDoListRepository toDoListRepository;

    public ResponseEntity<List<TaskDto>> getToDoList() throws ZeroTaskFoundException {
        List<TaskDto> toDoList = new ArrayList<>();

        if(toDoListRepository.findAll().isEmpty()) {
            throw new ZeroTaskFoundException("Zero task found.");
        }
        toDoListRepository.findAll().forEach(taskEntity -> {
            TaskDto taskDto = TaskMapper.INSTANCE.toDto(taskEntity);
            toDoList.add(taskDto);
        });
        return new ResponseEntity<>(toDoList, HttpStatus.OK);
    }

    public ResponseEntity<TaskDto> getTask(UUID id) throws TaskNotFoundException {
        if(!toDoListRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }

        TaskEntity taskEntity = toDoListRepository.findById(id).orElse(null);
        TaskDto taskDto = TaskMapper.INSTANCE.toDto(taskEntity);
        return new ResponseEntity<>(taskDto, HttpStatus.OK);
    }

    public void deleteTask(UUID id) throws TaskNotFoundException {
        if(!toDoListRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        toDoListRepository.deleteById(id);
    }

    public void addTask(TaskDto newTaskDto) {
        TaskEntity newTaskEntity = TaskMapper.INSTANCE.toEntity(newTaskDto);
        toDoListRepository.save(newTaskEntity);
    }

    public void updateTask(TaskDto updatedTaskDto, UUID id) throws TaskNotFoundException {
        if(!toDoListRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        TaskEntity updatedTaskEntity = TaskMapper.INSTANCE.toEntity(updatedTaskDto);
        toDoListRepository.save(updatedTaskEntity);
    }

}
