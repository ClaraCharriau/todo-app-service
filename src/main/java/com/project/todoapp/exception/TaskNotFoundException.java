package com.project.todoapp.exception;

import lombok.Getter;

import java.util.UUID;

public class TaskNotFoundException extends TodoListException {

    @Getter
    private final UUID id;

    public TaskNotFoundException(UUID id) {
        super("Task with id : " + id + " not found.");
        this.id = id;
    }
}
