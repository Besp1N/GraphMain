package com.kacper.backend.user;

import lombok.Builder;

import java.time.LocalDateTime;

/**
 * @param id is the id of the user
 * @param name is the name of the user
 * @param lastName is the last name of the user
 * @param email is the email of the user
 * @param role is the role of the user
 * @param createdAt is the date of creation of the user
 *
 * @Author Kacper Karabinowski
 */
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
