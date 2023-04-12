package com.project.todoapp.service;

import com.project.todoapp.dto.TaskDto;
import com.project.todoapp.exception.TaskNotFoundException;
import com.project.todoapp.mapper.TaskMapper;
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

    public List<TaskDto> getToDoList() {
        List<TaskDto> toDoList = new ArrayList<>();
        toDoListRepository.findAll().forEach(taskEntity -> {
            TaskDto taskDto = TaskMapper.INSTANCE.toDto(taskEntity);
            toDoList.add(taskDto);
        });
        return toDoList;
    }

    public TaskDto getTask(long id) throws TaskNotFoundException {
        TaskEntity taskEntity = toDoListRepository.findById(id).orElse(null);
        TaskDto taskDto = TaskMapper.INSTANCE.toDto(taskEntity);
        return taskDto;
    }

    public void deleteTask(long id) {
        toDoListRepository.deleteById(id);
    }

    public void addTask(TaskDto newTaskDto) {
        TaskEntity newTaskEntity = TaskMapper.INSTANCE.toEntity(newTaskDto);
        toDoListRepository.save(newTaskEntity);
    }

    public void updateTask(TaskDto updatedTaskDto, UUID id) {
        TaskEntity updatedTaskEntity = TaskMapper.INSTANCE.toEntity(updatedTaskDto);
        toDoListRepository.save(updatedTaskEntity);
    }

}
