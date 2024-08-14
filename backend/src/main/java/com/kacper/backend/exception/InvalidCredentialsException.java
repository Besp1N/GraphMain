package com.kacper.backend.exception;

public class InvalidCredentialsException extends RuntimeException
{
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
