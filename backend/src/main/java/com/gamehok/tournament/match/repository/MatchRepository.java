package com.gamehok.tournament.match.repository;

import com.gamehok.tournament.enums.MatchStatus;
import com.gamehok.tournament.match.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    Optional<Match> findByUuid(UUID uuid);

    List<Match> findByTournamentIdAndStageIdOrderByRoundNumberAscMatchNumberAsc(Long tournamentId, Long stageId);

    List<Match> findByTournamentIdAndStatus(Long tournamentId, MatchStatus status);

    @Query("SELECT m FROM Match m WHERE m.stageId = :stageId AND m.roundNumber = :round ORDER BY m.bracketPosition ASC")
    List<Match> findByStageAndRound(@Param("stageId") Long stageId, @Param("round") Integer round);

    @Query("SELECT m FROM Match m WHERE m.participant1Id = :participantId OR m.participant2Id = :participantId ORDER BY m.roundNumber ASC")
    List<Match> findByParticipantId(@Param("participantId") Long participantId);

    @Query("SELECT m FROM Match m WHERE m.status = 'SCHEDULED' AND m.scheduledAt <= :now")
    List<Match> findMatchesDueToStart(@Param("now") Instant now);

    @Query("SELECT m FROM Match m WHERE m.status = 'AWAITING_RESULT' AND m.completedAt < :cutoff")
    List<Match> findTimedOutResultSubmissions(@Param("cutoff") Instant cutoff);

    long countByTournamentIdAndStatus(Long tournamentId, MatchStatus status);

    @Query("SELECT COUNT(m) = 0 FROM Match m WHERE m.stageId = :stageId AND m.status != 'COMPLETED' AND m.status != 'BYE' AND m.status != 'WALKOVER'")
    boolean isStageComplete(@Param("stageId") Long stageId);
}
