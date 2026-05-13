package com.gamehok.tournament.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown when a requested resource cannot be found.
 * Maps to HTTP 404 Not Found.
 */
public class ResourceNotFoundException extends TournamentEngineException {

    public ResourceNotFoundException(String resourceType, Object identifier) {
        super(
                HttpStatus.NOT_FOUND,
                "RESOURCE_NOT_FOUND",
                String.format("%s not found with identifier: %s", resourceType, identifier)
        );
    }
}
