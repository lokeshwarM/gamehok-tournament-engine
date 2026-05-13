package com.gamehok.tournament.match.dto;

import com.gamehok.tournament.enums.MatchResultType;
import com.gamehok.tournament.enums.MatchStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

/**
 * Match detail response DTO.
 */
@Getter
@Builder
public class MatchResponseDto {

    private final UUID uuid;
    private final UUID tournamentUuid;
    private final Integer roundNumber;
    private final Integer matchNumber;
    private final Integer bracketPosition;
    private final UUID participant1Uuid;
    private final String participant1Name;
    private final UUID participant2Uuid;
    private final String participant2Name;
    private final UUID winnerUuid;
    private final Integer participant1Score;
    private final Integer participant2Score;
    private final MatchStatus status;
    private final MatchResultType resultType;
    private final Integer bestOf;
    private final Instant scheduledAt;
    private final Instant startedAt;
    private final Instant completedAt;
    private final boolean bye;
}
