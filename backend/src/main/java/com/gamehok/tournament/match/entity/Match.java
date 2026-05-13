package com.gamehok.tournament.match.entity;

import com.gamehok.tournament.common.entity.BaseEntity;
import com.gamehok.tournament.enums.MatchResultType;
import com.gamehok.tournament.enums.MatchStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * Match entity representing a single game/series between two participants.
 * <p>
 * Both participant slots can reference either userId or teamId depending on the tournament type.
 * The winner advances in the bracket; the loser is eliminated (or placed in losers' bracket for double elimination).
 * Scores are stored as JSON via {@code scoreJson} for flexibility across different game formats.
 * </p>
 */
@Entity
@Table(name = "matches",
        indexes = {
                @Index(name = "idx_matches_stage_id", columnList = "stage_id"),
                @Index(name = "idx_matches_status", columnList = "status"),
                @Index(name = "idx_matches_scheduled_at", columnList = "scheduled_at"),
                @Index(name = "idx_matches_participant1", columnList = "participant1_id"),
                @Index(name = "idx_matches_participant2", columnList = "participant2_id")
        }
)
@SequenceGenerator(name = "base_seq", sequenceName = "matches_id_seq", allocationSize = 50)
@Getter
@Setter
@NoArgsConstructor
public class Match extends BaseEntity {

    @Column(name = "tournament_id", nullable = false)
    private Long tournamentId;

    @Column(name = "stage_id", nullable = false)
    private Long stageId;

    @Column(name = "round_number", nullable = false)
    private Integer roundNumber;

    @Column(name = "match_number")
    private Integer matchNumber;

    @Column(name = "bracket_position")
    private Integer bracketPosition;

    @Column(name = "participant1_id")
    private Long participant1Id;

    @Column(name = "participant2_id")
    private Long participant2Id;

    @Column(name = "winner_participant_id")
    private Long winnerParticipantId;

    @Column(name = "loser_participant_id")
    private Long loserParticipantId;

    @Column(name = "participant1_score")
    private Integer participant1Score;

    @Column(name = "participant2_score")
    private Integer participant2Score;

    @Column(name = "score_json", columnDefinition = "TEXT")
    private String scoreJson;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private MatchStatus status = MatchStatus.SCHEDULED;

    @Enumerated(EnumType.STRING)
    @Column(name = "result_type", length = 30)
    private MatchResultType resultType;

    @Column(name = "best_of", nullable = false)
    private Integer bestOf = 1;

    @Column(name = "scheduled_at")
    private Instant scheduledAt;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    @Column(name = "next_match_id")
    private Long nextMatchId;

    @Column(name = "loser_next_match_id")
    private Long loserNextMatchId;

    @Column(name = "is_bye", nullable = false)
    private boolean bye = false;

    @Column(name = "notes", length = 1000)
    private String notes;
}
