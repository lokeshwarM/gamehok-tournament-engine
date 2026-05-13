package com.gamehok.tournament.tournament.exception;

import com.gamehok.tournament.common.exception.TournamentEngineException;
import org.springframework.http.HttpStatus;

/**
 * Tournament domain-specific exceptions.
 */
public class TournamentException extends TournamentEngineException {

    public TournamentException(String errorCode, String message) {
        super(HttpStatus.BAD_REQUEST, errorCode, message);
    }

    public TournamentException(HttpStatus status, String errorCode, String message) {
        super(status, errorCode, message);
    }

    public static TournamentException registrationClosed() {
        return new TournamentException("REGISTRATION_CLOSED", "Tournament registration is closed");
    }

    public static TournamentException tournamentFull() {
        return new TournamentException("TOURNAMENT_FULL", "Tournament has reached maximum participant capacity");
    }

    public static TournamentException alreadyRegistered() {
        return new TournamentException("ALREADY_REGISTERED", "Participant is already registered for this tournament");
    }

    public static TournamentException checkInNotOpen() {
        return new TournamentException("CHECK_IN_NOT_OPEN", "Check-in period is not currently open");
    }

    public static TournamentException alreadyCheckedIn() {
        return new TournamentException("ALREADY_CHECKED_IN", "Participant has already checked in");
    }

    public static TournamentException cannotCancel(String currentStatus) {
        return new TournamentException(HttpStatus.CONFLICT, "CANNOT_CANCEL_TOURNAMENT",
                "Cannot cancel tournament in status: " + currentStatus);
    }
}
