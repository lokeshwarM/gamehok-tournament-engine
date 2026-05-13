package com.gamehok.tournament.orchestration.registration;

import com.gamehok.tournament.common.exception.BusinessRuleViolationException;
import com.gamehok.tournament.common.exception.ResourceNotFoundException;
import com.gamehok.tournament.enums.ParticipantStatus;
import com.gamehok.tournament.enums.TournamentStatus;
import com.gamehok.tournament.tournament.entity.Tournament;
import com.gamehok.tournament.tournament.entity.TournamentParticipant;
import com.gamehok.tournament.tournament.repository.TournamentParticipantRepository;
import com.gamehok.tournament.tournament.repository.TournamentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

/**
 * Manages tournament participant registration with all business rule enforcement.
 *
 * <p>Business rules enforced here:
 * <ul>
 *   <li>Registration only allowed while tournament status is REGISTRATION_OPEN</li>
 *   <li>Registration window is [registrationStart, registrationEnd] (T-2h)</li>
 *   <li>Teams must match tournament-required teamSize</li>
 *   <li>Max participant capacity enforcement</li>
 *   <li>Duplicate registration prevention</li>
 *   <li>Time-overlap conflict detection (a user cannot join overlapping tournaments)</li>
 * </ul>
 *
 * <p>NOTE: team roster size is checked against {@link com.gamehok.tournament.team.repository.TeamMemberRepository}
 * at registration time, not just at seeding time.
 */
@Slf4j
@Service
public class RegistrationWorkflowService {

    private final TournamentRepository tournamentRepository;
    private final TournamentParticipantRepository participantRepository;
    private final OverlapConflictDetector overlapConflictDetector;
    private final TeamRosterValidator teamRosterValidator;

    public RegistrationWorkflowService(
            TournamentRepository tournamentRepository,
            TournamentParticipantRepository participantRepository,
            OverlapConflictDetector overlapConflictDetector,
            TeamRosterValidator teamRosterValidator
    ) {
        this.tournamentRepository = tournamentRepository;
        this.participantRepository = participantRepository;
        this.overlapConflictDetector = overlapConflictDetector;
        this.teamRosterValidator = teamRosterValidator;
    }

    /**
     * Registers a user (solo) for a tournament.
     *
     * @param tournamentUuid the tournament to register in
     * @param userId         the registering user's ID
     * @return the created TournamentParticipant
     */
    @Transactional
    public TournamentParticipant registerUser(UUID tournamentUuid, Long userId) {
        Tournament tournament = loadAndValidateRegistrationWindow(tournamentUuid);

        // Duplicate check
        if (participantRepository.existsByTournamentIdAndUserId(tournament.getId(), userId)) {
            throw new BusinessRuleViolationException("ALREADY_REGISTERED",
                    "User " + userId + " is already registered in tournament " + tournamentUuid);
        }

        // Capacity check
        long currentCount = participantRepository.countByTournamentId(tournament.getId());
        if (currentCount >= tournament.getMaxParticipants()) {
            throw new BusinessRuleViolationException("TOURNAMENT_FULL",
                    "Tournament '" + tournament.getName() + "' has reached maximum capacity.");
        }

        // Time-overlap conflict check
        overlapConflictDetector.validateNoUserOverlap(userId, tournament);

        TournamentParticipant participant = new TournamentParticipant();
        participant.setTournament(tournament);
        participant.setUserId(userId);
        participant.setStatus(ParticipantStatus.REGISTERED);
        participant.setRegisteredAt(Instant.now());

        TournamentParticipant saved = participantRepository.save(participant);
        log.info("[REGISTRATION] User {} registered in tournament {}", userId, tournamentUuid);
        return saved;
    }

    /**
     * Registers a team for a tournament.
     *
     * @param tournamentUuid the tournament to register in
     * @param teamId         the ID of the team to register
     * @param captainUserId  the user performing the registration (must be captain)
     * @return the created TournamentParticipant
     */
    @Transactional
    public TournamentParticipant registerTeam(UUID tournamentUuid, Long teamId, Long captainUserId) {
        Tournament tournament = loadAndValidateRegistrationWindow(tournamentUuid);

        // Team roster size validation
        teamRosterValidator.validateRosterSize(teamId, tournament.getTeamSize());

        // Duplicate check
        if (participantRepository.existsByTournamentIdAndTeamId(tournament.getId(), teamId)) {
            throw new BusinessRuleViolationException("ALREADY_REGISTERED",
                    "Team " + teamId + " is already registered in tournament " + tournamentUuid);
        }

        // Capacity check
        long currentCount = participantRepository.countByTournamentId(tournament.getId());
        if (currentCount >= tournament.getMaxParticipants()) {
            throw new BusinessRuleViolationException("TOURNAMENT_FULL",
                    "Tournament '" + tournament.getName() + "' has reached maximum capacity.");
        }

        // Time-overlap check for any team member
        overlapConflictDetector.validateNoTeamMemberOverlap(teamId, tournament);

        TournamentParticipant participant = new TournamentParticipant();
        participant.setTournament(tournament);
        participant.setTeamId(teamId);
        participant.setStatus(ParticipantStatus.REGISTERED);
        participant.setRegisteredAt(Instant.now());

        TournamentParticipant saved = participantRepository.save(participant);
        log.info("[REGISTRATION] Team {} registered in tournament {}", teamId, tournamentUuid);
        return saved;
    }

    /**
     * Withdraws a participant's registration (before tournament starts).
     */
    @Transactional
    public void withdraw(UUID tournamentUuid, Long userId) {
        Tournament tournament = tournamentRepository.findByUuid(tournamentUuid)
                .orElseThrow(() -> new ResourceNotFoundException("Tournament", tournamentUuid.toString()));

        if (tournament.getStatus() == TournamentStatus.IN_PROGRESS
                || tournament.getStatus() == TournamentStatus.COMPLETED) {
            throw new BusinessRuleViolationException("WITHDRAWAL_NOT_ALLOWED",
                    "Cannot withdraw from an in-progress or completed tournament.");
        }

        participantRepository.findByTournamentIdAndUserId(tournament.getId(), userId)
                .ifPresent(participant -> {
                    participant.setStatus(ParticipantStatus.WITHDRAWN);
                    participantRepository.save(participant);
                    log.info("[REGISTRATION] User {} withdrew from tournament {}", userId, tournamentUuid);
                });
    }

    /**
     * Loads a tournament and validates that the registration window is currently open.
     */
    private Tournament loadAndValidateRegistrationWindow(UUID tournamentUuid) {
        Tournament tournament = tournamentRepository.findByUuid(tournamentUuid)
                .orElseThrow(() -> new ResourceNotFoundException("Tournament", tournamentUuid.toString()));

        if (tournament.getStatus() != TournamentStatus.REGISTRATION_OPEN) {
            throw new BusinessRuleViolationException("REGISTRATION_CLOSED",
                    "Tournament '" + tournament.getName() + "' is not accepting registrations. Status: "
                            + tournament.getStatus());
        }

        Instant now = Instant.now();
        if (now.isBefore(tournament.getRegistrationStart())) {
            throw new BusinessRuleViolationException("REGISTRATION_NOT_STARTED",
                    "Registration for '" + tournament.getName() + "' has not started yet.");
        }
        if (now.isAfter(tournament.getRegistrationEnd())) {
            throw new BusinessRuleViolationException("REGISTRATION_EXPIRED",
                    "Registration for '" + tournament.getName() + "' has already closed.");
        }

        return tournament;
    }
}
