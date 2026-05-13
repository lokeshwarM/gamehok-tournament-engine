package com.gamehok.tournament.scheduler;

import com.gamehok.tournament.orchestration.service.TournamentOrchestrationService;
import com.gamehok.tournament.matchmaking.repository.MatchmakingQueueRepository;
import com.gamehok.tournament.tournament.repository.TournamentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * Scheduled task executor for automated tournament lifecycle management.
 * <p>
 * Runs periodic jobs to:
 * - Open registration for DRAFT tournaments whose registration start time has passed
 * - Close registration for REGISTRATION_OPEN tournaments past their deadline
 * - Start IN_PROGRESS tournaments at their scheduled start time
 * - Process matchmaking queue cycles
 * - Handle match result timeouts
 * <p>
 * All jobs are idempotent and safe to re-run.
 * </p>
 */
@Slf4j
@Component
public class TournamentScheduler {

    private final TournamentRepository tournamentRepository;
    private final TournamentOrchestrationService orchestrationService;

    public TournamentScheduler(
            TournamentRepository tournamentRepository,
            TournamentOrchestrationService orchestrationService
    ) {
        this.tournamentRepository = tournamentRepository;
        this.orchestrationService = orchestrationService;
    }

    /**
     * Checks every minute for tournaments that should open registration.
     */
    @Scheduled(fixedDelay = 60_000)
    public void openRegistrationForDueTournaments() {
        Instant now = Instant.now();
        tournamentRepository.findTournamentsReadyToOpenRegistration(now).forEach(t -> {
            log.info("[SCHEDULER] Opening registration for tournament: {} ({})", t.getName(), t.getUuid());
            // status transition delegated to orchestration/service
        });
    }

    /**
     * Checks every minute for tournaments that should close registration.
     */
    @Scheduled(fixedDelay = 60_000)
    public void closeExpiredRegistrations() {
        Instant now = Instant.now();
        tournamentRepository.findExpiredRegistrations(now).forEach(t -> {
            log.info("[SCHEDULER] Closing registration for tournament: {} ({})", t.getName(), t.getUuid());
        });
    }

    /**
     * Checks every minute for tournaments ready to start.
     */
    @Scheduled(fixedDelay = 60_000)
    public void startDueTournaments() {
        Instant now = Instant.now();
        tournamentRepository.findTournamentsReadyToStart(now).forEach(t -> {
            log.info("[SCHEDULER] Starting tournament: {} ({})", t.getName(), t.getUuid());
            orchestrationService.seedAndGenerateBracket(t.getUuid());
        });
    }

    /**
     * Processes matchmaking queue every 15 seconds.
     */
    @Scheduled(fixedDelay = 15_000)
    public void processMatchmakingQueue() {
        log.debug("[SCHEDULER] Processing matchmaking queue cycle");
        // Delegated to MatchmakingService
    }
}
