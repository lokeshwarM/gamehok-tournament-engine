package com.gamehok.tournament.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Base runtime exception for all domain-specific exceptions in the platform.
 * <p>
 * All module-specific exceptions MUST extend this class.
 * Carries an HTTP status code, machine-readable error code, and human-readable message.
 * </p>
 */
@Getter
public class TournamentEngineException extends RuntimeException {

    private final HttpStatus status;
    private final String errorCode;

    public TournamentEngineException(HttpStatus status, String errorCode, String message) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }

    public TournamentEngineException(HttpStatus status, String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.errorCode = errorCode;
    }
}
