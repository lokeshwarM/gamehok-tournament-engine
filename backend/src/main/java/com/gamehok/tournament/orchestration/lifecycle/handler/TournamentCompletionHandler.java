package com.gamehok.tournament.orchestration.lifecycle.handler;

import com.gamehok.tournament.orchestration.lifecycle.TransitionContext;
import com.gamehok.tournament.orchestration.lifecycle.TransitionHandler;
import com.gamehok.tournament.orchestration.service.TournamentOrchestrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Handles the IN_PROGRESS → COMPLETED transition side effects.
 *
 * <p>Finalizes the tournament:
 * <ol>
 *   <li>Computes final rankings from the last stage</li>
 *   <li>Triggers prize distribution</li>
 *   <li>Updates ELO ratings for all participants (via competitive rating service)</li>
 *   <li>Broadcasts completion notification to all participants</li>
 * </ol>
 */
@Slf4j
@Component
public class TournamentCompletionHandler implements TransitionHandler {

    private final TournamentOrchestrationService orchestrationService;

    public TournamentCompletionHandler(TournamentOrchestrationService orchestrationService) {
        this.orchestrationService = orchestrationService;
    }

    @Override
    @Async("tournamentAsyncExecutor")
    @Transactional
    public void onTransition(TransitionContext context) {
        log.info("[HANDLER] TournamentCompletion: finalizing tournament {}",
                context.tournament().getUuid());

        orchestrationService.completeTournament(context.tournament().getUuid());
    }
}
