package com.gamehok.tournament.orchestration.lifecycle.handler;

import com.gamehok.tournament.orchestration.lifecycle.TransitionContext;
import com.gamehok.tournament.orchestration.lifecycle.TransitionHandler;
import com.gamehok.tournament.orchestration.service.TournamentOrchestrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Handles the SEEDING → IN_PROGRESS transition side effects.
 *
 * <p>After seeding is complete and the tournament officially starts:
 * <ol>
 *   <li>Generates the initial bracket for the first stage</li>
 *   <li>Schedules all round 1 matches with calculated start times</li>
 *   <li>Notifies all participants with their match schedules</li>
 * </ol>
 */
@Slf4j
@Component
public class TournamentStartHandler implements TransitionHandler {

    private final TournamentOrchestrationService orchestrationService;

    public TournamentStartHandler(TournamentOrchestrationService orchestrationService) {
        this.orchestrationService = orchestrationService;
    }

    @Override
    @Async("tournamentAsyncExecutor")
    @Transactional
    public void onTransition(TransitionContext context) {
        log.info("[HANDLER] TournamentStart: generating bracket for tournament {}",
                context.tournament().getUuid());

        orchestrationService.seedAndGenerateBracket(context.tournament().getUuid());
    }
}
