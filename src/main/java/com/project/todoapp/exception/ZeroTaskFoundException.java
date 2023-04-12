package com.project.todoapp.exception;

import lombok.Getter;

public class ZeroTaskFoundException extends TodoListException {

    @Getter
    private final String message;

    public ZeroTaskFoundException(String message) {
        super(message);
        this.message = message;
    }

}
