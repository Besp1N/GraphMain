package com.kacper.backend.exception;

public class ResourceAlreadyExistException extends RuntimeException
{
    public ResourceAlreadyExistException(String message) {
        super(message);
    }
}
