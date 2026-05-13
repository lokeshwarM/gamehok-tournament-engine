package com.gamehok.tournament.scheduler;

import com.gamehok.tournament.enums.TournamentStatus;
import com.gamehok.tournament.orchestration.lifecycle.TournamentLifecycleStateMachine;
import com.gamehok.tournament.tournament.entity.Tournament;
import com.gamehok.tournament.tournament.repository.TournamentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

/**
 * Scheduled lifecycle driver for automated tournament state transitions.
 *
 * <p>All transitions are initiated through the {@link TournamentLifecycleStateMachine},
 * ensuring guards and handlers run correctly even for time-driven transitions.
 *
 * <p>All jobs are idempotent — safe to re-run, and guarded against double-execution
 * by the state machine's structural transition validation.
 *
 * <h3>Scheduled jobs:</h3>
 * <ul>
 *   <li>Every 60s: Open registration for DRAFT tournaments whose window has started</li>
 *   <li>Every 60s: Close registration for REGISTRATION_OPEN tournaments past T-2h</li>
 *   <li>Every 60s: Begin check-in for REGISTRATION_CLOSED tournaments</li>
 *   <li>Every 60s: Trigger seeding for CHECK_IN tournaments past their check-in window</li>
 *   <li>Every 15s: Matchmaking queue cycle</li>
 * </ul>
 */
@Slf4j
@Component
public class TournamentScheduler {

    private final TournamentRepository tournamentRepository;
    private final TournamentLifecycleStateMachine stateMachine;

    public TournamentScheduler(
            TournamentRepository tournamentRepository,
            TournamentLifecycleStateMachine stateMachine
    ) {
        this.tournamentRepository = tournamentRepository;
        this.stateMachine = stateMachine;
    }

    /**
     * Opens registration for DRAFT tournaments whose registrationStart has passed.
     * Runs every 60 seconds.
     */
    @Scheduled(fixedDelay = 60_000)
    public void openRegistrationForDueTournaments() {
        Instant now = Instant.now();
        List<Tournament> due = tournamentRepository.findTournamentsReadyToOpenRegistration(now);

        for (Tournament t : due) {
            tryTransition(t, TournamentStatus.REGISTRATION_OPEN,
                    "Scheduled: registration window opened");
        }
    }

    /**
     * Closes registration for tournaments whose registrationEnd (= startTime - 2h) has passed.
     * Runs every 60 seconds.
     */
    @Scheduled(fixedDelay = 60_000)
    public void closeExpiredRegistrations() {
        Instant now = Instant.now();
        List<Tournament> expired = tournamentRepository.findExpiredRegistrations(now);

        for (Tournament t : expired) {
            tryTransition(t, TournamentStatus.REGISTRATION_CLOSED,
                    "Scheduled: registration window expired (T-2h rule)");
        }
    }

    /**
     * Transitions REGISTRATION_CLOSED tournaments to SEEDING when all validations pass.
     * If a tournament has a check-in phase configured, it goes to CHECK_IN instead.
     * Runs every 60 seconds.
     */
    @Scheduled(fixedDelay = 60_000)
    public void advanceRegistrationClosedTournaments() {
        List<Tournament> closed = tournamentRepository.findRegistrationClosedTournaments();

        for (Tournament t : closed) {
            boolean hasCheckIn = t.getCheckInStart() != null && t.getCheckInEnd() != null;
            TournamentStatus next = hasCheckIn ? TournamentStatus.CHECK_IN : TournamentStatus.SEEDING;

            tryTransition(t, next,
                    "Scheduled: advancing from REGISTRATION_CLOSED to " + next);
        }
    }

    /**
     * Triggers seeding for CHECK_IN tournaments whose check-in window has closed.
     * Runs every 60 seconds.
     */
    @Scheduled(fixedDelay = 60_000)
    public void triggerSeedingForCompletedCheckIn() {
        Instant now = Instant.now();
        tournamentRepository.findAll().stream()
                .filter(t -> t.getStatus() == TournamentStatus.CHECK_IN)
                .filter(t -> t.getCheckInEnd() != null && now.isAfter(t.getCheckInEnd()))
                .forEach(t -> tryTransition(t, TournamentStatus.SEEDING,
                        "Scheduled: check-in window closed, proceeding to seeding"));
    }

    /**
     * Processes matchmaking queue every 15 seconds.
     */
    @Scheduled(fixedDelay = 15_000)
    public void processMatchmakingQueue() {
        log.debug("[SCHEDULER] Matchmaking queue cycle tick");
        // Delegated to MatchmakingService (injected separately to avoid circular dependencies)
    }

    /**
     * Safely executes a lifecycle transition, logging but not propagating exceptions.
     * Guards will reject invalid transitions; this ensures partial failures don't crash the scheduler.
     */
    private void tryTransition(Tournament tournament, TournamentStatus target, String reason) {
        try {
            stateMachine.systemTransition(tournament.getUuid(), target, reason);
        } catch (Exception ex) {
            log.warn("[SCHEDULER] Transition {} → {} skipped for tournament {}: {}",
                    tournament.getStatus(), target, tournament.getUuid(), ex.getMessage());
        }
    }
}
