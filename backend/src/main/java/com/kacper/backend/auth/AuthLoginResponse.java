package com.kacper.backend.auth;

import lombok.Builder;

@Builder
public record AuthLoginResponse(
        String token,
        String email,
        String role
) {
}
