package com.project.todoapp.exception;

import lombok.Getter;

import java.util.UUID;

public class TaskAlreadyExistException extends TodoListException {

    @Getter
    private final UUID id;

    public TaskAlreadyExistException(UUID id) {
        super("Task with id : " + id + " already exists.");
        this.id = id;
    }
}
