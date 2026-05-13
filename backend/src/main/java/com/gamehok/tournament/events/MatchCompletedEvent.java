package com.gamehok.tournament.events;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

/**
 * Domain event published when a match result is finalized.
 * <p>
 * Consumers:
 * - ProgressionEngine: advances bracket
 * - LeaderboardService: updates standings
 * - NotificationService: notifies participants
 * - ELO system: updates player ratings
 * </p>
 */
@Getter
@Builder
public class MatchCompletedEvent {

    private final UUID eventId;
    private final UUID matchUuid;
    private final UUID tournamentUuid;
    private final Long stageId;
    private final UUID winnerParticipantUuid;
    private final UUID loserParticipantUuid;
    private final Integer winnerScore;
    private final Integer loserScore;
    private final Instant occurredAt;
    private final String resultType;
}
