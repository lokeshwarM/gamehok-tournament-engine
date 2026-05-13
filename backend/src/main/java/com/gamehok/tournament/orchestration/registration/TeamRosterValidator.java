package com.gamehok.tournament.orchestration.registration;

import com.gamehok.tournament.common.exception.BusinessRuleViolationException;
import com.gamehok.tournament.team.repository.TeamMemberRepository;
import org.springframework.stereotype.Component;

/**
 * Validates that a team's active roster matches the tournament's required team size.
 *
 * <p>Called at registration time (not just at seeding time) to give teams early
 * feedback if their roster is incomplete.
 *
 * <p>Only active (non-substitute) members count toward the required size.
 */
@Component
public class TeamRosterValidator {

    private final TeamMemberRepository teamMemberRepository;

    public TeamRosterValidator(TeamMemberRepository teamMemberRepository) {
        this.teamMemberRepository = teamMemberRepository;
    }

    /**
     * Validates that a team has exactly the required number of active members.
     *
     * @param teamId       the team to validate
     * @param requiredSize the tournament's required team size (e.g., 5 for 5v5)
     * @throws BusinessRuleViolationException if roster size doesn't match
     */
    public void validateRosterSize(Long teamId, int requiredSize) {
        long activeCount = teamMemberRepository.countActiveByTeamId(teamId);

        if (activeCount != requiredSize) {
            throw new BusinessRuleViolationException("INVALID_ROSTER_SIZE",
                    String.format(
                            "Team [id=%d] has %d active members but this tournament requires exactly %d. " +
                            "Please update your roster before registering.",
                            teamId, activeCount, requiredSize
                    ));
        }
    }
}
