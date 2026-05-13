package com.gamehok.tournament.user.dto;

import com.gamehok.tournament.enums.UserRole;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

/**
 * Public user profile DTO returned by API responses.
 * Password hash and internal fields are never included.
 */
@Getter
@Builder
public class UserResponseDto {

    private final UUID uuid;
    private final String username;
    private final String email;
    private final String displayName;
    private final String avatarUrl;
    private final String gameId;
    private final UserRole role;
    private final Integer eloRating;
    private final boolean verified;
    private final String countryCode;
    private final Instant createdAt;
}
