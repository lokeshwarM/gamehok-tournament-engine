package com.gamehok.tournament.orchestration.lifecycle.guard;

import com.gamehok.tournament.common.exception.InvalidStateTransitionException;
import com.gamehok.tournament.enums.TeamType;
import com.gamehok.tournament.orchestration.lifecycle.TransitionContext;
import com.gamehok.tournament.orchestration.lifecycle.TransitionGuard;
import com.gamehok.tournament.team.repository.TeamMemberRepository;
import com.gamehok.tournament.tournament.entity.TournamentParticipant;
import com.gamehok.tournament.tournament.repository.TournamentParticipantRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Validates that every registered team has exactly the required number of members.
 *
 * <p>Business rule: if tournament requires 5v5, every registered team must have exactly
 * 5 active members. Teams with incorrect roster sizes are flagged and their registrations
 * are rejected (status set to DISQUALIFIED) before the transition proceeds.
 *
 * <p>Solo tournaments (teamSize=1) only require the player registration to exist.
 */
@Component
public class TeamSizeValidationGuard implements TransitionGuard {

    private final TournamentParticipantRepository participantRepository;
    private final TeamMemberRepository teamMemberRepository;

    public TeamSizeValidationGuard(
            TournamentParticipantRepository participantRepository,
            TeamMemberRepository teamMemberRepository
    ) {
        this.participantRepository = participantRepository;
        this.teamMemberRepository = teamMemberRepository;
    }

    @Override
    public void validate(TransitionContext context) {
        int requiredSize = context.tournament().getTeamSize();
        TeamType teamType = context.tournament().getTeamType();

        // Solo tournaments: each participant is a single user — no team roster to validate
        if (teamType == TeamType.SOLO || requiredSize == 1) {
            return;
        }

        List<TournamentParticipant> participants =
                participantRepository.findByTournamentIdOrderBySeedNumberAsc(context.tournament().getId());

        List<String> violations = new ArrayList<>();

        for (TournamentParticipant participant : participants) {
            if (participant.getTeamId() == null) continue;

            long memberCount = teamMemberRepository.countActiveByTeamId(participant.getTeamId());

            if (memberCount != requiredSize) {
                violations.add(String.format(
                        "Team [id=%d] has %d active members but tournament requires exactly %d",
                        participant.getTeamId(), memberCount, requiredSize));
            }
        }

        if (!violations.isEmpty()) {
            throw new InvalidStateTransitionException(String.format(
                    "Tournament '%s' has %d team(s) with invalid roster sizes: %s",
                    context.tournament().getUuid(), violations.size(), violations));
        }
    }
}
