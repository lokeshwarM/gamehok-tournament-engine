package com.gamehok.tournament.team.entity;

import com.gamehok.tournament.common.entity.BaseEntity;
import com.gamehok.tournament.enums.TeamType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Team aggregate entity representing a group of players competing together.
 * <p>
 * Supports variable team sizes from SOLO (1) to SQUAD (5+) via {@link TeamType}.
 * The captain (owner) has administrative control over the team roster.
 * </p>
 */
@Entity
@Table(name = "teams",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_teams_name", columnNames = "name"),
                @UniqueConstraint(name = "uk_teams_tag", columnNames = "tag")
        },
        indexes = {
                @Index(name = "idx_teams_captain_id", columnList = "captain_id")
        }
)
@SequenceGenerator(name = "base_seq", sequenceName = "teams_id_seq", allocationSize = 50)
@Getter
@Setter
@NoArgsConstructor
public class Team extends BaseEntity {

    @Column(name = "name", nullable = false, length = 80)
    private String name;

    @Column(name = "tag", nullable = false, length = 10)
    private String tag;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    @Column(name = "captain_id", nullable = false)
    private Long captainId;

    @Enumerated(EnumType.STRING)
    @Column(name = "team_type", nullable = false, length = 20)
    private TeamType teamType;

    @Column(name = "max_size", nullable = false)
    private Integer maxSize;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(name = "country_code", length = 3)
    private String countryCode;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TeamMember> members = new ArrayList<>();
}
