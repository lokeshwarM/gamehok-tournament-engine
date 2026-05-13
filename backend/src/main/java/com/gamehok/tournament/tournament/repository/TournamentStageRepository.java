package com.gamehok.tournament.tournament.repository;

import com.gamehok.tournament.tournament.entity.TournamentStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for TournamentStage entity.
 */
@Repository
public interface TournamentStageRepository extends JpaRepository<TournamentStage, Long> {

    Optional<TournamentStage> findByUuid(UUID uuid);

    List<TournamentStage> findByTournamentIdOrderByStageOrderAsc(Long tournamentId);

    Optional<TournamentStage> findFirstByTournamentIdOrderByStageOrderAsc(Long tournamentId);

    long countByTournamentIdAndCompletedTrue(Long tournamentId);

    long countByTournamentId(Long tournamentId);
}
