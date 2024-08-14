package com.kacper.backend.auth;

public record AuthLoginRequest(
        String email,
        String password
) {
}
