package com.gamehok.tournament.events;

import com.gamehok.tournament.enums.TournamentStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

/**
 * Domain event published when a tournament transitions between lifecycle states.
 * <p>
 * Consumers:
 * - NotificationService: notifies all registered participants
 * - SchedulerService: updates scheduled jobs
 * - OrchestrationService: triggers automated workflows
 * </p>
 */
@Getter
@Builder
public class TournamentStatusChangedEvent {

    private final UUID eventId;
    private final UUID tournamentUuid;
    private final String tournamentName;
    private final TournamentStatus previousStatus;
    private final TournamentStatus newStatus;
    private final String changedBy;
    private final String reason;
    private final Instant occurredAt;
}
