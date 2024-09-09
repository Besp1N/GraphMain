package com.kacper.backend.auth;

/**
 * @param email user email
 * @param password user password
 *
 * @Author Kacper Karabinowski
 */
public record AuthLoginRequest(
        String email,
        String password
) {
}
