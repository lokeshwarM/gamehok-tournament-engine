package com.gamehok.tournament.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown when a user attempts an action they are not authorized to perform.
 * Maps to HTTP 403 Forbidden.
 */
public class AccessDeniedException extends TournamentEngineException {

    public AccessDeniedException(String message) {
        super(HttpStatus.FORBIDDEN, "ACCESS_DENIED", message);
    }
}
