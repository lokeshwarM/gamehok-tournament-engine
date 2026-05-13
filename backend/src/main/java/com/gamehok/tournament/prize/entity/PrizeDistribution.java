package com.gamehok.tournament.prize.entity;

import com.gamehok.tournament.common.entity.BaseEntity;
import com.gamehok.tournament.enums.PrizeType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Records prize distribution to a winning participant.
 */
@Entity
@Table(name = "prize_distributions",
        indexes = {
                @Index(name = "idx_prize_dist_participant", columnList = "participant_id"),
                @Index(name = "idx_prize_dist_tournament", columnList = "tournament_id")
        }
)
@SequenceGenerator(name = "base_seq", sequenceName = "prize_distributions_id_seq", allocationSize = 20)
@Getter
@Setter
@NoArgsConstructor
public class PrizeDistribution extends BaseEntity {

    @Column(name = "tournament_id", nullable = false)
    private Long tournamentId;

    @Column(name = "participant_id", nullable = false)
    private Long participantId;

    @Column(name = "rank_achieved", nullable = false)
    private Integer rankAchieved;

    @Enumerated(EnumType.STRING)
    @Column(name = "prize_type", nullable = false, length = 20)
    private PrizeType prizeType;

    @Column(name = "cash_amount", precision = 12, scale = 2)
    private BigDecimal cashAmount;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "distributed_at")
    private Instant distributedAt;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "PENDING";

    @Column(name = "reference_id", length = 100)
    private String referenceId;
}
