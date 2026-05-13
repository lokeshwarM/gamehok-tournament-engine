package com.gamehok.tournament.tournament.dto;

import com.gamehok.tournament.enums.*;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Tournament summary DTO returned in list responses.
 */
@Getter
@Builder
public class TournamentSummaryDto {

    private final UUID uuid;
    private final String name;
    private final String slug;
    private final String bannerUrl;
    private final TournamentType tournamentType;
    private final TournamentStatus status;
    private final TeamType teamType;
    private final Integer teamSize;
    private final Integer maxParticipants;
    private final Integer registeredCount;
    private final String gameTitle;
    private final String platform;
    private final String region;
    private final Instant startTime;
    private final BigDecimal prizePool;
    private final boolean featured;
}
