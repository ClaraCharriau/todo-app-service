package com.project.todoapp.service;

import com.project.todoapp.dto.TaskDto;
import com.project.todoapp.exception.TaskNotFoundException;
import com.project.todoapp.exception.ZeroTaskFoundException;
import com.project.todoapp.mapper.TaskMapper;
import com.project.todoapp.model.TaskEntity;
import com.project.todoapp.repository.ToDoListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ToDoListService {

    @Autowired
    private ToDoListRepository toDoListRepository;

    public List<TaskDto> getToDoList() throws ZeroTaskFoundException {
        if(toDoListRepository.findAll().isEmpty()) {
            throw new ZeroTaskFoundException("Zero task found.");
        }
        return TaskMapper.INSTANCE.toDto(toDoListRepository.findAll());
    }

    public TaskDto getTask(UUID id) throws TaskNotFoundException {
        if(!toDoListRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        TaskEntity taskEntity = toDoListRepository.findById(id).orElse(null);
        return TaskMapper.INSTANCE.toDto(taskEntity);
    }

    public void deleteTask(UUID id) throws TaskNotFoundException {
        if(!toDoListRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        toDoListRepository.deleteById(id);
    }

    public UUID createTask(TaskDto newTaskDto) {
        TaskEntity newTaskEntity = TaskMapper.INSTANCE.toEntity(newTaskDto);
        var savedTask = toDoListRepository.save(newTaskEntity);
        return savedTask.getId();
    }

    public void updateTask(TaskDto updatedTaskDto, UUID id) throws TaskNotFoundException {
        if(!toDoListRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        TaskEntity updatedTaskEntity = TaskMapper.INSTANCE.toEntity(updatedTaskDto);
        toDoListRepository.save(updatedTaskEntity);
    }

}
