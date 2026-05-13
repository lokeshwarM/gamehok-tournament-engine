package com.gamehok.tournament.matchmaking.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

/**
 * Response after joining or checking matchmaking queue status.
 */
@Getter
@Builder
public class QueueStatusDto {

    private final UUID queueUuid;
    private final String status;
    private final Integer eloRating;
    private final Integer eloWindowLow;
    private final Integer eloWindowHigh;
    private final Instant joinedAt;
    private final Long estimatedWaitSeconds;
    private final UUID matchedMatchUuid;
}
