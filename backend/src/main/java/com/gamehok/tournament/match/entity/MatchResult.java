package com.gamehok.tournament.match.entity;

import com.gamehok.tournament.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * Records a result submission for a match, supporting dispute tracking.
 * Each participant can submit their own result, and disputes are flagged when results differ.
 */
@Entity
@Table(name = "match_results",
        indexes = {
                @Index(name = "idx_match_results_match_id", columnList = "match_id")
        }
)
@SequenceGenerator(name = "base_seq", sequenceName = "match_results_id_seq", allocationSize = 50)
@Getter
@Setter
@NoArgsConstructor
public class MatchResult extends BaseEntity {

    @Column(name = "match_id", nullable = false)
    private Long matchId;

    @Column(name = "submitted_by_participant_id", nullable = false)
    private Long submittedByParticipantId;

    @Column(name = "reported_winner_id", nullable = false)
    private Long reportedWinnerId;

    @Column(name = "score_json", columnDefinition = "TEXT")
    private String scoreJson;

    @Column(name = "screenshot_url", length = 500)
    private String screenshotUrl;

    @Column(name = "submitted_at", nullable = false)
    private Instant submittedAt;

    @Column(name = "is_disputed", nullable = false)
    private boolean disputed = false;

    @Column(name = "dispute_reason", length = 1000)
    private String disputeReason;
}
