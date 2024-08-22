package com.kacper.backend.auth;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AuthRegistrationRequest(
        @Email(message = "Invalid email")
        @NotBlank(message = "Email cannot be empty")
        String email,

        @NotBlank(message = "Password cannot be empty")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        @Pattern(regexp = ".*[!@#$%^&*(),.?\":{}|<>].*", message = "Password must contain at least one special character")
        @Pattern(regexp = "^\\p{ASCII}*$", message = "Password cannot contain emojis")
        String password,

        @NotBlank(message = "Role cannot be empty")
        @Pattern(regexp = "ADMIN|USER", message = "Role must be either ADMIN or USER")
        String role,

        @NotBlank(message = "Name cannot be empty")
        @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters long")
        String name,

        @NotBlank(message = "Last name cannot be empty")
        @Size(min = 3, max = 50, message = "Last name must be between 3 and 50 characters long")
        String lastName
) {
}
