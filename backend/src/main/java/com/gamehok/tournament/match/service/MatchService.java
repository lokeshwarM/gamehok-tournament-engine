package com.gamehok.tournament.match.service;

import com.gamehok.tournament.match.dto.MatchResponseDto;
import com.gamehok.tournament.match.dto.SubmitResultRequest;

import java.util.List;
import java.util.UUID;

/**
 * Match domain service contract.
 */
public interface MatchService {

    MatchResponseDto getMatchByUuid(UUID uuid);

    List<MatchResponseDto> getMatchesByTournamentAndStage(UUID tournamentUuid, UUID stageUuid);

    List<MatchResponseDto> getMatchesByParticipant(UUID participantUuid);

    MatchResponseDto submitResult(UUID matchUuid, SubmitResultRequest request);

    MatchResponseDto disputeResult(UUID matchUuid, String reason);

    MatchResponseDto resolveDispute(UUID matchUuid, UUID winnerId, String adminNote);

    MatchResponseDto adminOverrideResult(UUID matchUuid, UUID winnerId, String reason);

    void startMatch(UUID matchUuid);
}
