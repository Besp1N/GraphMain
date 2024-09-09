package com.kacper.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Handler for ResourceAlreadyExistException returns status 409 (CONFLICT)
 *
 * @Author Kacper Karabinowski
 */
@ControllerAdvice
public class ResourceAlreadyExistHandler
{
    @ExceptionHandler(ResourceAlreadyExistException.class)
    public ResponseEntity<String> handleResourceAlreadyExistException(ResourceAlreadyExistException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }
}
