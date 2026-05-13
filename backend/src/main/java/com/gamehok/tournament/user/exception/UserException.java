package com.gamehok.tournament.user.exception;

import com.gamehok.tournament.common.exception.TournamentEngineException;
import org.springframework.http.HttpStatus;

/**
 * Thrown when user-specific business rules are violated.
 */
public class UserException extends TournamentEngineException {

    public UserException(String errorCode, String message) {
        super(HttpStatus.BAD_REQUEST, errorCode, message);
    }

    public static UserException userNotVerified() {
        return new UserException("USER_NOT_VERIFIED", "User account is not verified");
    }

    public static UserException userBanned() {
        return new UserException("USER_BANNED", "User account is banned");
    }
}
