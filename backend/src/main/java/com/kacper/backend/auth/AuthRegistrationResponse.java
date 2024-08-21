package com.kacper.backend.auth;

import lombok.Builder;

@Builder
public record AuthRegistrationResponse(
        String name,
        String lastName,
        String email,
        String role
) {
}
