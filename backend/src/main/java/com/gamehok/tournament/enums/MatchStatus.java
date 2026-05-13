package com.gamehok.tournament.enums;

/**
 * Lifecycle states of an individual match.
 */
public enum MatchStatus {

    SCHEDULED,
    CHECK_IN_PENDING,
    READY,
    IN_PROGRESS,
    AWAITING_RESULT,
    DISPUTED,
    COMPLETED,
    WALKOVER,
    BYE,
    CANCELLED,
    POSTPONED
}
