package com.gamehok.tournament.tournament.repository;

import com.gamehok.tournament.enums.ParticipantStatus;
import com.gamehok.tournament.tournament.entity.TournamentParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TournamentParticipantRepository extends JpaRepository<TournamentParticipant, Long> {

    Optional<TournamentParticipant> findByTournamentIdAndUserId(Long tournamentId, Long userId);

    Optional<TournamentParticipant> findByTournamentIdAndTeamId(Long tournamentId, Long teamId);

    List<TournamentParticipant> findByTournamentIdOrderBySeedNumberAsc(Long tournamentId);

    long countByTournamentId(Long tournamentId);

    long countByTournamentIdAndStatus(Long tournamentId, ParticipantStatus status);

    @Query("SELECT p FROM TournamentParticipant p WHERE p.tournament.id = :tournamentId AND p.status = :status ORDER BY p.finalRank ASC")
    List<TournamentParticipant> findByTournamentIdAndStatusOrderByRank(
            @Param("tournamentId") Long tournamentId,
            @Param("status") ParticipantStatus status
    );

    boolean existsByTournamentIdAndUserId(Long tournamentId, Long userId);

    boolean existsByTournamentIdAndTeamId(Long tournamentId, Long teamId);

    /**
     * Finds all participants for a tournament with a specific status.
     * Used by the seeding pipeline to load only eligible (REGISTERED / CHECKED_IN) participants.
     */
    @Query("SELECT p FROM TournamentParticipant p WHERE p.tournament.id = :tournamentId AND p.status = :status")
    List<TournamentParticipant> findByTournamentIdAndStatus(
            @Param("tournamentId") Long tournamentId,
            @Param("status") ParticipantStatus status
    );
}
