package com.gamehok.tournament.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown when an illegal state transition is attempted on a domain aggregate.
 * Maps to HTTP 409 Conflict.
 * <p>
 * Examples:
 * - Transitioning tournament from COMPLETED → IN_PROGRESS
 * - Submitting match result for an already-completed match
 * </p>
 */
public class InvalidStateTransitionException extends TournamentEngineException {

    public InvalidStateTransitionException(String entity, String fromState, String toState) {
        super(
                HttpStatus.CONFLICT,
                "INVALID_STATE_TRANSITION",
                String.format("Cannot transition %s from '%s' to '%s'", entity, fromState, toState)
        );
    }

    /**
     * Convenience constructor for guard-level violations where a descriptive message is sufficient.
     */
    public InvalidStateTransitionException(String message) {
        super(HttpStatus.CONFLICT, "INVALID_STATE_TRANSITION", message);
    }
}
