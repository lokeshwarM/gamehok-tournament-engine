package com.gamehok.tournament.matchmaking.repository;

import com.gamehok.tournament.matchmaking.entity.MatchmakingQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MatchmakingQueueRepository extends JpaRepository<MatchmakingQueue, Long> {

    Optional<MatchmakingQueue> findByUuid(UUID uuid);

    Optional<MatchmakingQueue> findByUserIdAndStatus(Long userId, String status);

    @Query("""
            SELECT q FROM MatchmakingQueue q
            WHERE q.gameTitle = :gameTitle
              AND q.region = :region
              AND q.status = 'SEARCHING'
              AND q.eloRating BETWEEN :eloLow AND :eloHigh
            ORDER BY q.joinedAt ASC
            """)
    List<MatchmakingQueue> findMatchableCandidates(
            @Param("gameTitle") String gameTitle,
            @Param("region") String region,
            @Param("eloLow") Integer eloLow,
            @Param("eloHigh") Integer eloHigh
    );

    @Query("SELECT q FROM MatchmakingQueue q WHERE q.status = 'SEARCHING' ORDER BY q.joinedAt ASC")
    List<MatchmakingQueue> findAllSearchingOrderByJoinTime();
}
