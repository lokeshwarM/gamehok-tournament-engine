package com.gamehok.tournament.leaderboard.service;

import com.gamehok.tournament.leaderboard.dto.LeaderboardEntryDto;

import java.util.List;
import java.util.UUID;

/**
 * Leaderboard service contract.
 */
public interface LeaderboardService {

    List<LeaderboardEntryDto> getStageStandings(UUID tournamentUuid, UUID stageUuid);

    List<LeaderboardEntryDto> getTournamentStandings(UUID tournamentUuid);

    void recalculateStageStandings(Long stageId);

    void updateEntryAfterMatch(Long matchId);
}
