package com.gamehok.tournament.leaderboard.entity;

import com.gamehok.tournament.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Leaderboard standing entry for a participant within a tournament stage.
 * Tracks wins, losses, points, kills, and placement.
 */
@Entity
@Table(name = "leaderboard_entries",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_leaderboard_stage_participant",
                        columnNames = {"stage_id", "participant_id"})
        },
        indexes = {
                @Index(name = "idx_leaderboard_stage_points", columnList = "stage_id, points DESC"),
                @Index(name = "idx_leaderboard_tournament", columnList = "tournament_id")
        }
)
@SequenceGenerator(name = "base_seq", sequenceName = "leaderboard_id_seq", allocationSize = 100)
@Getter
@Setter
@NoArgsConstructor
public class LeaderboardEntry extends BaseEntity {

    @Column(name = "tournament_id", nullable = false)
    private Long tournamentId;

    @Column(name = "stage_id", nullable = false)
    private Long stageId;

    @Column(name = "participant_id", nullable = false)
    private Long participantId;

    @Column(name = "rank")
    private Integer rank;

    @Column(name = "points", nullable = false)
    private Integer points = 0;

    @Column(name = "wins", nullable = false)
    private Integer wins = 0;

    @Column(name = "losses", nullable = false)
    private Integer losses = 0;

    @Column(name = "draws", nullable = false)
    private Integer draws = 0;

    @Column(name = "kills")
    private Integer kills;

    @Column(name = "deaths")
    private Integer deaths;

    @Column(name = "goal_difference")
    private Integer goalDifference;

    @Column(name = "matches_played", nullable = false)
    private Integer matchesPlayed = 0;

    @Column(name = "is_qualified", nullable = false)
    private boolean qualified = false;
}
