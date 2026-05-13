package com.gamehok.tournament.user.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * Authentication response containing JWT access and refresh tokens.
 */
@Getter
@Builder
public class AuthResponse {

    private final String accessToken;
    private final String refreshToken;
    private final String tokenType;
    private final long expiresIn;
    private final UserResponseDto user;
}
