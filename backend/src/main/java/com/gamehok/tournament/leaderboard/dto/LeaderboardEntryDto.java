package com.gamehok.tournament.leaderboard.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

/**
 * Leaderboard entry response DTO.
 */
@Getter
@Builder
public class LeaderboardEntryDto {

    private final Integer rank;
    private final UUID participantUuid;
    private final String participantName;
    private final String avatarUrl;
    private final Integer points;
    private final Integer wins;
    private final Integer losses;
    private final Integer draws;
    private final Integer matchesPlayed;
    private final Integer kills;
    private final Integer goalDifference;
    private final boolean qualified;
}
