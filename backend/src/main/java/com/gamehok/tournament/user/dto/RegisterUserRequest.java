package com.gamehok.tournament.user.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Request DTO for user registration.
 */
@Getter
@NoArgsConstructor
public class RegisterUserRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_.-]+$", message = "Username can only contain alphanumeric characters, underscores, dots, and hyphens")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Must be a valid email address")
    @Size(max = 150)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must be at least 8 characters")
    private String password;

    @Size(max = 80, message = "Display name must not exceed 80 characters")
    private String displayName;

    @Size(max = 100)
    private String gameId;

    @Size(min = 2, max = 3, message = "Country code must be 2-3 characters")
    private String countryCode;
}
