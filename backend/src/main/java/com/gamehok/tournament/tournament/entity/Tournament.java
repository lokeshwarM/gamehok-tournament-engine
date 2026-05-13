package com.gamehok.tournament.tournament.entity;

import com.gamehok.tournament.common.entity.BaseEntity;
import com.gamehok.tournament.enums.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Tournament aggregate root entity.
 * <p>
 * Represents a tournament event configured with a specific format, team type,
 * registration window, and prize pool. Orchestration state is managed separately
 * through the orchestration engine.
 * <p>
 * Status transitions are enforced by the {@link com.gamehok.tournament.orchestration} module.
 * </p>
 */
@Entity
@Table(name = "tournaments",
        indexes = {
                @Index(name = "idx_tournaments_status", columnList = "status"),
                @Index(name = "idx_tournaments_organizer_id", columnList = "organizer_id"),
                @Index(name = "idx_tournaments_start_time", columnList = "start_time"),
                @Index(name = "idx_tournaments_type_status", columnList = "tournament_type, status")
        }
)
@SequenceGenerator(name = "base_seq", sequenceName = "tournaments_id_seq", allocationSize = 10)
@Getter
@Setter
@NoArgsConstructor
public class Tournament extends BaseEntity {

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "slug", nullable = false, unique = true, length = 200)
    private String slug;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "banner_url", length = 500)
    private String bannerUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "tournament_type", nullable = false, length = 30)
    private TournamentType tournamentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private TournamentStatus status = TournamentStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "team_type", nullable = false, length = 20)
    private TeamType teamType;

    @Column(name = "team_size", nullable = false)
    private Integer teamSize;

    @Column(name = "min_participants", nullable = false)
    private Integer minParticipants;

    @Column(name = "max_participants", nullable = false)
    private Integer maxParticipants;

    @Column(name = "registration_start", nullable = false)
    private Instant registrationStart;

    @Column(name = "registration_end", nullable = false)
    private Instant registrationEnd;

    @Column(name = "check_in_start")
    private Instant checkInStart;

    @Column(name = "check_in_end")
    private Instant checkInEnd;

    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    @Column(name = "end_time")
    private Instant endTime;

    @Column(name = "game_title", nullable = false, length = 100)
    private String gameTitle;

    @Column(name = "game_mode", length = 100)
    private String gameMode;

    @Column(name = "platform", length = 50)
    private String platform;

    @Column(name = "region", length = 50)
    private String region;

    @Column(name = "organizer_id", nullable = false)
    private Long organizerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "seeding_strategy", length = 30)
    private SeedingStrategy seedingStrategy = SeedingStrategy.RANDOM;

    @Column(name = "is_featured", nullable = false)
    private boolean featured = false;

    @Column(name = "is_public", nullable = false)
    private boolean publicTournament = true;

    @Column(name = "entry_fee")
    private java.math.BigDecimal entryFee;

    @Column(name = "prize_pool")
    private java.math.BigDecimal prizePool;

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("stageOrder ASC")
    private List<TournamentStage> stages = new ArrayList<>();

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TournamentParticipant> participants = new ArrayList<>();
}
