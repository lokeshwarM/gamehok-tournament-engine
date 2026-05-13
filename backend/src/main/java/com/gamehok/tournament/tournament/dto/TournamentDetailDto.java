package com.gamehok.tournament.tournament.dto;

import com.gamehok.tournament.enums.*;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Full tournament detail DTO including stages and participant counts.
 */
@Getter
@Builder
public class TournamentDetailDto {

    private final UUID uuid;
    private final String name;
    private final String slug;
    private final String description;
    private final String bannerUrl;
    private final TournamentType tournamentType;
    private final TournamentStatus status;
    private final TeamType teamType;
    private final Integer teamSize;
    private final Integer minParticipants;
    private final Integer maxParticipants;
    private final Integer registeredCount;
    private final Integer checkedInCount;
    private final Instant registrationStart;
    private final Instant registrationEnd;
    private final Instant checkInStart;
    private final Instant checkInEnd;
    private final Instant startTime;
    private final Instant endTime;
    private final String gameTitle;
    private final String gameMode;
    private final String platform;
    private final String region;
    private final SeedingStrategy seedingStrategy;
    private final boolean featured;
    private final boolean publicTournament;
    private final BigDecimal entryFee;
    private final BigDecimal prizePool;
    private final List<TournamentStageDto> stages;
    private final Instant createdAt;
}
