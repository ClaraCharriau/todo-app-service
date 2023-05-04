package com.project.todoapp.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorMessage {

    private String code;
    private String message;
    private String requestedURI;

}
