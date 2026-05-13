package com.gamehok.tournament.leaderboard.repository;

import com.gamehok.tournament.leaderboard.entity.LeaderboardEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaderboardRepository extends JpaRepository<LeaderboardEntry, Long> {

    Optional<LeaderboardEntry> findByStageIdAndParticipantId(Long stageId, Long participantId);

    @Query("SELECT e FROM LeaderboardEntry e WHERE e.stageId = :stageId ORDER BY e.points DESC, e.wins DESC, e.goalDifference DESC")
    List<LeaderboardEntry> findStageStandingsOrdered(@Param("stageId") Long stageId);

    @Query("SELECT e FROM LeaderboardEntry e WHERE e.tournamentId = :tournamentId ORDER BY e.points DESC")
    List<LeaderboardEntry> findTournamentStandings(@Param("tournamentId") Long tournamentId);

    List<LeaderboardEntry> findByStageIdAndQualifiedTrue(Long stageId);

    long countByStageId(Long stageId);
}
