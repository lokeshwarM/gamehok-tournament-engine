package com.gamehok.tournament.orchestration.registration;

import com.gamehok.tournament.common.exception.BusinessRuleViolationException;
import com.gamehok.tournament.enums.TournamentStatus;
import com.gamehok.tournament.team.repository.TeamMemberRepository;
import com.gamehok.tournament.tournament.entity.Tournament;
import com.gamehok.tournament.tournament.repository.TournamentParticipantRepository;
import com.gamehok.tournament.tournament.repository.TournamentRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * Detects scheduling conflicts when a user or team tries to register in a tournament
 * whose active time window overlaps with another tournament they are already in.
 *
 * <p>Business rule: a user cannot be an active participant in two tournaments
 * whose [startTime, endTime] windows overlap, even if one hasn't started yet.
 *
 * <p>Overlap definition:
 * <pre>
 * Two windows [A_start, A_end] and [B_start, B_end] overlap if:
 *   A_start < B_end AND A_end > B_start
 * (standard interval overlap, exclusive boundaries)
 * </pre>
 */
@Component
public class OverlapConflictDetector {

    private static final Set<TournamentStatus> ACTIVE_STATUSES = Set.of(
            TournamentStatus.REGISTRATION_OPEN,
            TournamentStatus.REGISTRATION_CLOSED,
            TournamentStatus.CHECK_IN,
            TournamentStatus.SEEDING,
            TournamentStatus.IN_PROGRESS
    );

    private final TournamentRepository tournamentRepository;
    private final TournamentParticipantRepository participantRepository;
    private final TeamMemberRepository teamMemberRepository;

    public OverlapConflictDetector(
            TournamentRepository tournamentRepository,
            TournamentParticipantRepository participantRepository,
            TeamMemberRepository teamMemberRepository
    ) {
        this.tournamentRepository = tournamentRepository;
        this.participantRepository = participantRepository;
        this.teamMemberRepository = teamMemberRepository;
    }

    /**
     * Validates that the given user is not registered in any tournament that overlaps
     * in time with the candidate tournament.
     *
     * @throws BusinessRuleViolationException if a time conflict is detected
     */
    public void validateNoUserOverlap(Long userId, Tournament candidate) {
        List<Tournament> activeTournaments = tournamentRepository.findActiveUserTournaments(
                userId, ACTIVE_STATUSES);

        for (Tournament existing : activeTournaments) {
            if (timeWindowsOverlap(existing, candidate)) {
                throw new BusinessRuleViolationException("SCHEDULE_CONFLICT",
                        String.format(
                                "User %d is already registered in tournament '%s' (starts %s, ends %s) " +
                                "which overlaps with '%s' (starts %s, ends %s).",
                                userId,
                                existing.getName(), existing.getStartTime(), existing.getEndTime(),
                                candidate.getName(), candidate.getStartTime(), candidate.getEndTime()
                        ));
            }
        }
    }

    /**
     * Validates that no member of the given team is registered in an overlapping tournament.
     *
     * @throws BusinessRuleViolationException if any team member has a time conflict
     */
    public void validateNoTeamMemberOverlap(Long teamId, Tournament candidate) {
        List<Long> memberUserIds = teamMemberRepository.findActiveUserIdsByTeamId(teamId);
        for (Long userId : memberUserIds) {
            validateNoUserOverlap(userId, candidate);
        }
    }

    /**
     * Determines if two tournament time windows overlap.
     * Uses {@code endTime} if present, otherwise estimates using start + 24 hours.
     */
    private boolean timeWindowsOverlap(Tournament a, Tournament b) {
        var aStart = a.getStartTime();
        var aEnd = a.getEndTime() != null ? a.getEndTime() : aStart.plusSeconds(86400);
        var bStart = b.getStartTime();
        var bEnd = b.getEndTime() != null ? b.getEndTime() : bStart.plusSeconds(86400);

        return aStart.isBefore(bEnd) && aEnd.isAfter(bStart);
    }
}
