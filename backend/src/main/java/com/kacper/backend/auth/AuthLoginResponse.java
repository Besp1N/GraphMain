package com.kacper.backend.auth;

import lombok.Builder;

/**
 * @param token jwt token
 * @param email user email
 * @param role user role
 *
 * @Author Kacper Karabinowski
 */
@Builder
public record AuthLoginResponse(
        String token,
        String email,
        String role
) {
}
