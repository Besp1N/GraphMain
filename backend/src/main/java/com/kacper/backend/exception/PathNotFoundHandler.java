package com.kacper.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * Handles exceptions related to paths that are not found.
 * NOT included BAD REQUEST exception.
 *
 * @author Kacper Karabinowski
 */
@ControllerAdvice
public class PathNotFoundHandler
{
    /**
     * @param ex exception
     * @return response entity with message and status 404 (NOT FOUND)
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        return new ResponseEntity<>("Chyba cos Ci sie pomylilo panie kolego", HttpStatus.NOT_FOUND);
    }
}
