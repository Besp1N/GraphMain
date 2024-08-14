package com.kacper.backend.auth;


public record AuthRegistrationRequest(
        String email,
        String password,
        String role,
        String name,
        String lastName
) {
}
