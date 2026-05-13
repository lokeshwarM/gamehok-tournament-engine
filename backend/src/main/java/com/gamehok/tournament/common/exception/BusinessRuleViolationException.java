package com.gamehok.tournament.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown when a requested business operation violates domain rules.
 * Maps to HTTP 422 Unprocessable Entity.
 * <p>
 * Examples:
 * - Registering for a tournament that is already full
 * - Attempting to start a tournament before the registration closes
 * - Advancing a match result on a completed tournament
 * </p>
 */
public class BusinessRuleViolationException extends TournamentEngineException {

    public BusinessRuleViolationException(String errorCode, String message) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, errorCode, message);
    }
}
