package com.gamehok.tournament.tournament.entity;

import com.gamehok.tournament.common.entity.BaseEntity;
import com.gamehok.tournament.enums.ParticipantStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * Represents a participant's registration and lifecycle within a specific tournament.
 * <p>
 * A participant can be either an individual player (solo) or a team (squad/duo).
 * Exactly one of {@code userId} or {@code teamId} should be populated based on
 * the tournament's {@link com.gamehok.tournament.enums.TeamType}.
 * </p>
 */
@Entity
@Table(name = "tournament_participants",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_participants_tournament_team",
                        columnNames = {"tournament_id", "team_id"}),
                @UniqueConstraint(name = "uk_participants_tournament_user",
                        columnNames = {"tournament_id", "user_id"})
        },
        indexes = {
                @Index(name = "idx_participants_tournament_id", columnList = "tournament_id"),
                @Index(name = "idx_participants_status", columnList = "status")
        }
)
@SequenceGenerator(name = "base_seq", sequenceName = "tournament_participants_id_seq", allocationSize = 50)
@Getter
@Setter
@NoArgsConstructor
public class TournamentParticipant extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tournament_id", nullable = false, foreignKey = @ForeignKey(name = "fk_participants_tournament"))
    private Tournament tournament;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "team_id")
    private Long teamId;

    @Column(name = "seed_number")
    private Integer seedNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private ParticipantStatus status = ParticipantStatus.REGISTERED;

    @Column(name = "registered_at", nullable = false)
    private Instant registeredAt;

    @Column(name = "checked_in_at")
    private Instant checkedInAt;

    @Column(name = "eliminated_at")
    private Instant eliminatedAt;

    @Column(name = "final_rank")
    private Integer finalRank;

    @Column(name = "points_earned", nullable = false)
    private Integer pointsEarned = 0;
}
