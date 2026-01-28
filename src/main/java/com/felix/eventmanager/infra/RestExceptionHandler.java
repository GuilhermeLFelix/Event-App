package com.felix.eventmanager.infra;

import com.felix.eventmanager.exceptions.EventDuplicatedException;
import com.felix.eventmanager.exceptions.EventInvalidHourException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EventDuplicatedException.class)
    private ResponseEntity<String> eventDuplicatedHandler(EventDuplicatedException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(EventInvalidHourException.class)
    private ResponseEntity<String> eventInvalidHourHandler(EventInvalidHourException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
}
