package com.gamehok.tournament.team.exception;

import com.gamehok.tournament.common.exception.TournamentEngineException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

/**
 * Team-specific domain exception.
 */
public class TeamException extends TournamentEngineException {

    public TeamException(String errorCode, String message) {
        super(HttpStatus.BAD_REQUEST, errorCode, message);
    }

    public static TeamException teamFull(String teamName) {
        return new TeamException("TEAM_FULL", "Team '" + teamName + "' has reached maximum capacity");
    }

    public static TeamException alreadyMember(UUID userUuid) {
        return new TeamException("ALREADY_TEAM_MEMBER", "User " + userUuid + " is already a member of this team");
    }

    public static TeamException notCaptain() {
        return new TeamException("NOT_CAPTAIN", "Only the team captain can perform this action");
    }
}
