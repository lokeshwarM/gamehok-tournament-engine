package com.gamehok.tournament.enums;

/**
 * Lifecycle states of a tournament from creation to archival.
 */
public enum TournamentStatus {

    DRAFT,
    REGISTRATION_OPEN,
    REGISTRATION_CLOSED,
    CHECK_IN,
    SEEDING,
    IN_PROGRESS,
    PAUSED,
    COMPLETED,
    CANCELLED,
    ARCHIVED
}
