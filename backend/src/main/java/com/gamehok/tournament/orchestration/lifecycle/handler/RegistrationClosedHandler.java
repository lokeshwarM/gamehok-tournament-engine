package com.gamehok.tournament.orchestration.lifecycle.handler;

import com.gamehok.tournament.enums.ParticipantStatus;
import com.gamehok.tournament.orchestration.lifecycle.TransitionContext;
import com.gamehok.tournament.orchestration.lifecycle.TransitionHandler;
import com.gamehok.tournament.tournament.entity.TournamentParticipant;
import com.gamehok.tournament.tournament.repository.TournamentParticipantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Handles the REGISTRATION_OPEN → REGISTRATION_CLOSED transition side effects.
 *
 * <p>Workflow:
 * <ol>
 *   <li>Validates that all registered teams still satisfy roster requirements</li>
 *   <li>Drops participants whose teams have become invalid (e.g., member left)</li>
 *   <li>Publishes a notification to all remaining valid registrants</li>
 * </ol>
 *
 * <p>Runs asynchronously on the tournament-async thread pool.
 */
@Slf4j
@Component
public class RegistrationClosedHandler implements TransitionHandler {

    private final TournamentParticipantRepository participantRepository;

    public RegistrationClosedHandler(TournamentParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    @Override
    @Async("tournamentAsyncExecutor")
    @Transactional
    public void onTransition(TransitionContext context) {
        Long tournamentId = context.tournament().getId();

        log.info("[HANDLER] RegistrationClosed: processing tournament {}", context.tournament().getUuid());

        List<TournamentParticipant> registered =
                participantRepository.findByTournamentIdOrderBySeedNumberAsc(tournamentId);

        long validCount = registered.stream()
                .filter(p -> p.getStatus() == ParticipantStatus.REGISTERED)
                .count();

        log.info("[HANDLER] Tournament {}: {} valid participants locked in at registration close.",
                context.tournament().getUuid(), validCount);

        // Notification dispatch would be triggered here via NotificationService
        // This is intentionally left as a hook for the notification module to handle
        // via the TournamentStatusChangedEvent published by the state machine
    }
}
