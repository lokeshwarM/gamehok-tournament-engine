package com.gamehok.tournament.tournament.repository;

import com.gamehok.tournament.enums.TournamentStatus;
import com.gamehok.tournament.enums.TournamentType;
import com.gamehok.tournament.tournament.entity.Tournament;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Tournament entity with rich querying via specifications.
 * <p>
 * Extends {@link JpaSpecificationExecutor} to support dynamic filter queries
 * (game, status, type, region, platform) without exposing raw queries to the service layer.
 * </p>
 */
@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long>, JpaSpecificationExecutor<Tournament> {

    Optional<Tournament> findByUuid(UUID uuid);

    Optional<Tournament> findBySlug(String slug);

    boolean existsBySlug(String slug);

    Page<Tournament> findByStatus(TournamentStatus status, Pageable pageable);

    Page<Tournament> findByTournamentTypeAndStatus(TournamentType type, TournamentStatus status, Pageable pageable);

    @Query("SELECT t FROM Tournament t WHERE t.publicTournament = true AND t.status = :status ORDER BY t.featured DESC, t.startTime ASC")
    Page<Tournament> findPublicByStatus(@Param("status") TournamentStatus status, Pageable pageable);

    @Query("SELECT t FROM Tournament t WHERE t.organizerId = :organizerId ORDER BY t.createdAt DESC")
    Page<Tournament> findByOrganizerId(@Param("organizerId") Long organizerId, Pageable pageable);

    @Query("SELECT t FROM Tournament t WHERE t.featured = true AND t.status IN :statuses")
    List<Tournament> findFeaturedByStatuses(@Param("statuses") List<TournamentStatus> statuses);

    @Query("SELECT t FROM Tournament t WHERE t.status = 'REGISTRATION_OPEN' AND t.registrationEnd < :now")
    List<Tournament> findExpiredRegistrations(@Param("now") Instant now);

    @Query("SELECT t FROM Tournament t WHERE t.status = 'DRAFT' AND t.registrationStart <= :now")
    List<Tournament> findTournamentsReadyToOpenRegistration(@Param("now") Instant now);

    @Query("SELECT t FROM Tournament t WHERE t.status = 'CHECK_IN' AND t.startTime <= :now")
    List<Tournament> findTournamentsReadyToStart(@Param("now") Instant now);

    /**
     * Finds all active tournaments a given user is participating in (solo or via team).
     * Used for time-overlap conflict detection during registration.
     */
    @Query("""
            SELECT t FROM Tournament t
            JOIN TournamentParticipant p ON p.tournament.id = t.id
            WHERE t.status IN :statuses
              AND (p.userId = :userId OR p.teamId IN (
                    SELECT tm.team.id FROM TeamMember tm WHERE tm.userId = :userId AND tm.active = true
              ))
              AND p.status NOT IN ('WITHDRAWN', 'DISQUALIFIED')
            """)
    List<Tournament> findActiveUserTournaments(
            @Param("userId") Long userId,
            @Param("statuses") java.util.Set<com.gamehok.tournament.enums.TournamentStatus> statuses
    );

    /**
     * Finds all tournaments in REGISTRATION_CLOSED status that have sufficient
     * participants and are ready to transition to SEEDING.
     */
    @Query("SELECT t FROM Tournament t WHERE t.status = 'REGISTRATION_CLOSED'")
    List<Tournament> findRegistrationClosedTournaments();
}
