package com.gamehok.tournament.orchestration.lifecycle.handler;

import com.gamehok.tournament.orchestration.lifecycle.TransitionContext;
import com.gamehok.tournament.orchestration.lifecycle.TransitionHandler;
import com.gamehok.tournament.orchestration.seeding.SeedingOrchestrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Handles the → SEEDING transition side effect.
 *
 * <p>Triggers the seeding orchestration pipeline:
 * <ol>
 *   <li>Computes competitive ratings for all participants</li>
 *   <li>Categorizes participants into STRONG / MODERATE / WEAK tiers</li>
 *   <li>Applies the format-appropriate seeding policy</li>
 *   <li>Assigns seed numbers to {@link com.gamehok.tournament.tournament.entity.TournamentParticipant}</li>
 * </ol>
 *
 * <p>Runs asynchronously — bracket generation proceeds after seeding completes.
 */
@Slf4j
@Component
public class SeedingPreparationHandler implements TransitionHandler {

    private final SeedingOrchestrationService seedingOrchestrationService;

    public SeedingPreparationHandler(SeedingOrchestrationService seedingOrchestrationService) {
        this.seedingOrchestrationService = seedingOrchestrationService;
    }

    @Override
    @Async("tournamentAsyncExecutor")
    @Transactional
    public void onTransition(TransitionContext context) {
        log.info("[HANDLER] SeedingPreparation: starting seeding pipeline for tournament {}",
                context.tournament().getUuid());

        seedingOrchestrationService.executeSeedingPipeline(context.tournament().getUuid());

        log.info("[HANDLER] SeedingPreparation: seeding complete for tournament {}",
                context.tournament().getUuid());
    }
}
