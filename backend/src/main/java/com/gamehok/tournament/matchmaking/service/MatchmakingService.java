package com.gamehok.tournament.matchmaking.service;

import com.gamehok.tournament.matchmaking.dto.JoinQueueRequest;
import com.gamehok.tournament.matchmaking.dto.QueueStatusDto;

import java.util.UUID;

/**
 * Matchmaking queue service contract.
 * Handles quick game matchmaking outside of structured tournaments.
 */
public interface MatchmakingService {

    QueueStatusDto joinQueue(JoinQueueRequest request);

    QueueStatusDto getQueueStatus(UUID queueUuid);

    void leaveQueue(UUID queueUuid);

    void processMatchmakingCycle();
}
