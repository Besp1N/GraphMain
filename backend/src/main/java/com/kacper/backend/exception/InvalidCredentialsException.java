package com.kacper.backend.exception;

/**
 * Exception thrown when the credentials are invalid
 *
 * @Author Kacper Karabinowski
 */
public class InvalidCredentialsException extends RuntimeException
{
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
