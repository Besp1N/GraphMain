package com.kacper.backend.exception;

/**
 * Exception thrown when the resource already exists
 *
 * @Author Kacper Karabinowski
 */
public class ResourceAlreadyExistException extends RuntimeException
{
    public ResourceAlreadyExistException(String message) {
        super(message);
    }
}
