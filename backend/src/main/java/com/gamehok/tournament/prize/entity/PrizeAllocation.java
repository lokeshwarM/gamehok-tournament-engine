package com.gamehok.tournament.prize.entity;

import com.gamehok.tournament.common.entity.BaseEntity;
import com.gamehok.tournament.enums.PrizeType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Prize pool allocation entity defining prizes per rank placement.
 */
@Entity
@Table(name = "prize_allocations",
        indexes = {
                @Index(name = "idx_prize_tournament_id", columnList = "tournament_id"),
                @Index(name = "idx_prize_rank", columnList = "tournament_id, rank_placement")
        }
)
@SequenceGenerator(name = "base_seq", sequenceName = "prize_allocations_id_seq", allocationSize = 20)
@Getter
@Setter
@NoArgsConstructor
public class PrizeAllocation extends BaseEntity {

    @Column(name = "tournament_id", nullable = false)
    private Long tournamentId;

    @Column(name = "rank_placement", nullable = false)
    private Integer rankPlacement;

    @Enumerated(EnumType.STRING)
    @Column(name = "prize_type", nullable = false, length = 20)
    private PrizeType prizeType;

    @Column(name = "cash_amount", precision = 12, scale = 2)
    private BigDecimal cashAmount;

    @Column(name = "credit_amount")
    private Integer creditAmount;

    @Column(name = "item_description", length = 500)
    private String itemDescription;

    @Column(name = "trophy_name", length = 200)
    private String trophyName;

    @Column(name = "badge_code", length = 100)
    private String badgeCode;

    @Column(name = "is_distributed", nullable = false)
    private boolean distributed = false;
}
