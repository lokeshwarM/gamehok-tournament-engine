package com.gamehok.tournament.team.entity;

import com.gamehok.tournament.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Join entity representing a user's membership in a team.
 * Tracks role within the team (captain, member, substitute).
 */
@Entity
@Table(name = "team_members",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_team_members_team_user", columnNames = {"team_id", "user_id"})
        },
        indexes = {
                @Index(name = "idx_team_members_user_id", columnList = "user_id")
        }
)
@SequenceGenerator(name = "base_seq", sequenceName = "team_members_id_seq", allocationSize = 50)
@Getter
@Setter
@NoArgsConstructor
public class TeamMember extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team_id", nullable = false, foreignKey = @ForeignKey(name = "fk_team_members_team"))
    private Team team;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "jersey_number")
    private Integer jerseyNumber;

    @Column(name = "is_captain", nullable = false)
    private boolean captain = false;

    @Column(name = "is_substitute", nullable = false)
    private boolean substitute = false;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;
}
