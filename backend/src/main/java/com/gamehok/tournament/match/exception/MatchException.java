package com.gamehok.tournament.match.exception;

import com.gamehok.tournament.common.exception.TournamentEngineException;
import org.springframework.http.HttpStatus;

/**
 * Match domain exceptions.
 */
public class MatchException extends TournamentEngineException {

    public MatchException(String errorCode, String message) {
        super(HttpStatus.BAD_REQUEST, errorCode, message);
    }

    public static MatchException matchNotStarted() {
        return new MatchException("MATCH_NOT_STARTED", "Match has not started yet");
    }

    public static MatchException alreadyCompleted() {
        return new MatchException("MATCH_ALREADY_COMPLETED", "Match is already completed");
    }

    public static MatchException invalidWinner() {
        return new MatchException("INVALID_WINNER", "Reported winner is not a participant in this match");
    }

    public static MatchException resultAlreadySubmitted() {
        return new MatchException("RESULT_ALREADY_SUBMITTED", "Result has already been submitted for this match");
    }
}
