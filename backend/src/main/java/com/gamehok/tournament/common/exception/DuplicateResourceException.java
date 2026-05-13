package com.gamehok.tournament.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown when a duplicate resource creation is attempted.
 * Maps to HTTP 409 Conflict.
 */
public class DuplicateResourceException extends TournamentEngineException {

    public DuplicateResourceException(String resourceType, String field, Object value) {
        super(
                HttpStatus.CONFLICT,
                "DUPLICATE_RESOURCE",
                String.format("%s already exists with %s: %s", resourceType, field, value)
        );
    }
}
