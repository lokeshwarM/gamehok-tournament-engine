package com.gamehok.tournament.tournament.entity;

import com.gamehok.tournament.common.entity.BaseEntity;
import com.gamehok.tournament.enums.StageType;
import com.gamehok.tournament.enums.TournamentStatus;
import com.gamehok.tournament.enums.TournamentType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a discrete phase/stage within a multi-stage tournament.
 * <p>
 * A HYBRID tournament might consist of:
 *   Stage 1: GROUP_STAGE (league format, type=LEAGUE)
 *   Stage 2: QUARTER_FINAL (knockout, type=KNOCKOUT)
 *   Stage 3: SEMI_FINAL (knockout, type=KNOCKOUT)
 *   Stage 4: GRAND_FINAL (knockout, type=KNOCKOUT)
 * <p>
 * Each stage has its own qualification rules defining how many advance to the next stage.
 * </p>
 */
@Entity
@Table(name = "tournament_stages",
        indexes = {
                @Index(name = "idx_tournament_stages_tournament_id", columnList = "tournament_id"),
                @Index(name = "idx_tournament_stages_order", columnList = "tournament_id, stage_order")
        }
)
@SequenceGenerator(name = "base_seq", sequenceName = "tournament_stages_id_seq", allocationSize = 20)
@Getter
@Setter
@NoArgsConstructor
public class TournamentStage extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tournament_id", nullable = false, foreignKey = @ForeignKey(name = "fk_stages_tournament"))
    private Tournament tournament;

    @Column(name = "stage_name", nullable = false, length = 100)
    private String stageName;

    @Enumerated(EnumType.STRING)
    @Column(name = "stage_type", nullable = false, length = 30)
    private StageType stageType;

    @Enumerated(EnumType.STRING)
    @Column(name = "format", nullable = false, length = 30)
    private TournamentType format;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private TournamentStatus status = TournamentStatus.DRAFT;

    @Column(name = "stage_order", nullable = false)
    private Integer stageOrder;

    @Column(name = "participant_count", nullable = false)
    private Integer participantCount;

    @Column(name = "qualifiers_count")
    private Integer qualifiersCount;

    @Column(name = "best_of", nullable = false)
    private Integer bestOf = 1;

    @Column(name = "double_elimination", nullable = false)
    private boolean doubleElimination = false;

    @Column(name = "third_place_match", nullable = false)
    private boolean thirdPlaceMatch = false;

    @Column(name = "is_completed", nullable = false)
    private boolean completed = false;
}
