package com.project.todoapp.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Handle exceptions for this service.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({TaskNotFoundException.class, ZeroTaskFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public @ResponseBody ErrorMessage handleTaskNotFound(Exception e, HttpServletRequest request) {

        LOGGER.error("Handling " + e.getClass().getSimpleName() + " due to " + e.getMessage());

        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setCode("404");
        errorMessage.setMessage(e.getMessage());
        errorMessage.setRequestedURI(request.getRequestURI());

        return errorMessage;
    }

}
