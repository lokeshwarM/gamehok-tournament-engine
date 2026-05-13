package com.gamehok.tournament.team.dto;

import com.gamehok.tournament.enums.TeamType;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Team profile response DTO.
 */
@Getter
@Builder
public class TeamResponseDto {

    private final UUID uuid;
    private final String name;
    private final String tag;
    private final String description;
    private final String logoUrl;
    private final UUID captainUuid;
    private final TeamType teamType;
    private final Integer maxSize;
    private final Integer currentSize;
    private final String countryCode;
    private final List<TeamMemberDto> members;
    private final Instant createdAt;
}
