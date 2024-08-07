package com.kacper.backend.exception;

/**
 * Resource not found exception class.
 *
 * @author Kacper Karabinowski
 */
public class ResourceNotFoundException extends RuntimeException
{
    /**
     * @param message message to be displayed
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
