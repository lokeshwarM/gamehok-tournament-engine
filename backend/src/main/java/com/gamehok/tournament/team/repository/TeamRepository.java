package com.gamehok.tournament.team.repository;

import com.gamehok.tournament.team.entity.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    Optional<Team> findByUuid(UUID uuid);

    Optional<Team> findByName(String name);

    boolean existsByName(String name);

    boolean existsByTag(String tag);

    @Query("SELECT t FROM Team t WHERE t.captainId = :captainId AND t.active = true")
    Page<Team> findByCaptainId(@Param("captainId") Long captainId, Pageable pageable);

    @Query("SELECT t FROM Team t WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :query, '%')) AND t.active = true")
    Page<Team> searchByName(@Param("query") String query, Pageable pageable);
}
