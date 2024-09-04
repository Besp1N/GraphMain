package com.kacper.backend.user;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserResponse(
    Integer id,
    String name,
    String lastName,
    String email,
    String role,
    LocalDateTime createdAt
) {
}
