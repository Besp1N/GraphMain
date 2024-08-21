package com.kacper.backend.auth;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthRegistrationRequest(
        @Email(message = "Invalid email")
        @NotBlank(message = "Email cannot be empty")
        String email,

        @NotBlank(message = "Password cannot be empty")
        String password,

        @NotBlank(message = "Role cannot be empty")
        String role,

        @NotBlank(message = "Name cannot be empty")
        String name,

        @NotBlank(message = "Last name cannot be empty")
        String lastName
) {
}
