package com.gamehok.tournament.events;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

/**
 * Domain event published when a participant registers for or is eliminated from a tournament.
 */
@Getter
@Builder
public class ParticipantStatusChangedEvent {

    private final UUID eventId;
    private final UUID tournamentUuid;
    private final UUID participantUuid;
    private final boolean isTeam;
    private final String previousStatus;
    private final String newStatus;
    private final Instant occurredAt;
}
