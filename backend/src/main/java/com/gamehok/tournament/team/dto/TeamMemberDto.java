package com.gamehok.tournament.team.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

/**
 * Team member summary DTO embedded in {@link TeamResponseDto}.
 */
@Getter
@Builder
public class TeamMemberDto {

    private final UUID userUuid;
    private final String username;
    private final String displayName;
    private final String avatarUrl;
    private final boolean captain;
    private final boolean substitute;
    private final Integer jerseyNumber;
}
