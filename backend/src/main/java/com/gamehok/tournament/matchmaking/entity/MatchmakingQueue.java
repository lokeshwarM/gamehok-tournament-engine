package com.gamehok.tournament.matchmaking.entity;

import com.gamehok.tournament.common.entity.BaseEntity;
import com.gamehok.tournament.enums.TeamType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * Represents a player/team actively queued for quick matchmaking.
 * <p>
 * Matchmaking queues are game/mode/region specific.
 * ELO window expands over time to prevent indefinite wait times.
 * </p>
 */
@Entity
@Table(name = "matchmaking_queue",
        indexes = {
                @Index(name = "idx_mmq_game_mode_region", columnList = "game_title, game_mode, region"),
                @Index(name = "idx_mmq_user_id", columnList = "user_id"),
                @Index(name = "idx_mmq_joined_at", columnList = "joined_at"),
                @Index(name = "idx_mmq_status", columnList = "status")
        }
)
@SequenceGenerator(name = "base_seq", sequenceName = "matchmaking_queue_id_seq", allocationSize = 100)
@Getter
@Setter
@NoArgsConstructor
public class MatchmakingQueue extends BaseEntity {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "team_id")
    private Long teamId;

    @Enumerated(EnumType.STRING)
    @Column(name = "team_type", nullable = false, length = 20)
    private TeamType teamType;

    @Column(name = "game_title", nullable = false, length = 100)
    private String gameTitle;

    @Column(name = "game_mode", length = 100)
    private String gameMode;

    @Column(name = "region", length = 50)
    private String region;

    @Column(name = "elo_rating", nullable = false)
    private Integer eloRating;

    @Column(name = "elo_window_low", nullable = false)
    private Integer eloWindowLow;

    @Column(name = "elo_window_high", nullable = false)
    private Integer eloWindowHigh;

    @Column(name = "joined_at", nullable = false)
    private Instant joinedAt;

    @Column(name = "matched_at")
    private Instant matchedAt;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "SEARCHING";

    @Column(name = "matched_match_id")
    private Long matchedMatchId;
}
