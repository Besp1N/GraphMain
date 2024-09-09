package com.kacper.backend.auth;

import lombok.Builder;

/**
 * Response to the registration request
 *
 * @param name user name
 * @param lastName user last name
 * @param email user email
 * @param role user role
 *
 * @Author Kacper Karabinowski
 */
@Builder
public record AuthRegistrationResponse(
        String name,
        String lastName,
        String email,
        String role
) {
}
