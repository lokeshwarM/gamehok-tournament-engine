package com.gamehok.tournament.team.repository;

import com.gamehok.tournament.team.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for TeamMember entity.
 * Provides active roster queries used by registration and seeding validation.
 */
@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

    /**
     * Counts active (non-substitute, is_active=true) members of a team.
     * Used by roster size validation at registration and seeding guard time.
     */
    @Query("SELECT COUNT(tm) FROM TeamMember tm WHERE tm.team.id = :teamId AND tm.active = true AND tm.substitute = false")
    long countActiveByTeamId(@Param("teamId") Long teamId);

    /**
     * Returns ELO ratings of all active members of a team.
     * Used by the competitive rating engine for team score computation.
     */
    @Query("""
            SELECT u.eloRating FROM TeamMember tm
            JOIN User u ON u.id = tm.userId
            WHERE tm.team.id = :teamId AND tm.active = true
            """)
    List<Integer> findActiveElosByTeamId(@Param("teamId") Long teamId);

    /**
     * Returns user IDs of all active members in a team.
     * Used for overlap conflict detection during team registration.
     */
    @Query("SELECT tm.userId FROM TeamMember tm WHERE tm.team.id = :teamId AND tm.active = true")
    List<Long> findActiveUserIdsByTeamId(@Param("teamId") Long teamId);

    List<TeamMember> findByTeamIdAndActiveTrue(Long teamId);

    boolean existsByTeamIdAndUserIdAndActiveTrue(Long teamId, Long userId);
}
