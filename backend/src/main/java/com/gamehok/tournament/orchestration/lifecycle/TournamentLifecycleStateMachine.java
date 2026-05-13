package com.gamehok.tournament.orchestration.lifecycle;

import com.gamehok.tournament.common.exception.InvalidStateTransitionException;
import com.gamehok.tournament.enums.TournamentStatus;
import com.gamehok.tournament.events.TournamentStatusChangedEvent;
import com.gamehok.tournament.tournament.entity.Tournament;
import com.gamehok.tournament.tournament.repository.TournamentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Central state machine executor for the tournament lifecycle.
 *
 * <p>This service is the <em>only</em> place in the codebase where
 * {@link Tournament#setStatus(TournamentStatus)} is called. All other code
 * must go through this service to change tournament status.
 *
 * <p>Execution order for every transition:
 * <ol>
 *   <li>Validate the transition exists in {@link TournamentLifecycleTransitions}</li>
 *   <li>Run all registered {@link TransitionGuard}s for this transition</li>
 *   <li>Persist the new status atomically</li>
 *   <li>Publish {@link TournamentStatusChangedEvent} via Spring Events</li>
 *   <li>Invoke registered {@link TransitionHandler}s (may be async)</li>
 * </ol>
 */
@Slf4j
@Service
public class TournamentLifecycleStateMachine {

    private final TournamentRepository tournamentRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * Guard registry: maps (from → to) pair to the list of guards that must pass.
     * Guards are registered by injecting them from the Spring context via the constructor.
     */
    private final Map<TransitionKey, List<TransitionGuard>> guardRegistry;

    /**
     * Handler registry: maps (from → to) pair to the list of post-transition handlers.
     */
    private final Map<TransitionKey, List<TransitionHandler>> handlerRegistry;

    public TournamentLifecycleStateMachine(
            TournamentRepository tournamentRepository,
            ApplicationEventPublisher eventPublisher,
            TournamentLifecycleGuardRegistry guardRegistry,
            TournamentLifecycleHandlerRegistry handlerRegistry
    ) {
        this.tournamentRepository = tournamentRepository;
        this.eventPublisher = eventPublisher;
        this.guardRegistry = guardRegistry.buildGuardMap();
        this.handlerRegistry = handlerRegistry.buildHandlerMap();
    }

    /**
     * Executes a tournament lifecycle transition atomically.
     *
     * @param tournamentUuid UUID of the tournament to transition
     * @param targetStatus   the desired next state
     * @param triggeredBy    principal or system actor that triggered this transition
     * @param reason         human-readable reason for audit trail
     * @throws InvalidStateTransitionException if the transition is not allowed or guards fail
     */
    @Transactional
    public void transition(UUID tournamentUuid, TournamentStatus targetStatus, String triggeredBy, String reason) {
        Tournament tournament = tournamentRepository.findByUuid(tournamentUuid)
                .orElseThrow(() -> new InvalidStateTransitionException(
                        "Tournament not found: " + tournamentUuid));

        TournamentStatus currentStatus = tournament.getStatus();

        // 1. Validate the structural transition is defined
        if (!TournamentLifecycleTransitions.isAllowed(currentStatus, targetStatus)) {
            throw new InvalidStateTransitionException(String.format(
                    "Tournament '%s' cannot transition from %s to %s",
                    tournament.getUuid(), currentStatus, targetStatus));
        }

        TransitionContext context = new TransitionContext(tournament, targetStatus, triggeredBy, reason);

        // 2. Run all guards — any failure throws and aborts
        TransitionKey key = TransitionKey.of(currentStatus, targetStatus);
        List<TransitionGuard> guards = guardRegistry.getOrDefault(key, List.of());
        for (TransitionGuard guard : guards) {
            guard.validate(context);
        }

        // 3. Persist the new status
        TournamentStatus previousStatus = tournament.getStatus();
        tournament.setStatus(targetStatus);
        tournamentRepository.save(tournament);

        log.info("[LIFECYCLE] Tournament {} transitioned {} → {} (by: {}, reason: {})",
                tournamentUuid, previousStatus, targetStatus, triggeredBy, reason);

        // 4. Publish domain event
        TournamentStatusChangedEvent event = TournamentStatusChangedEvent.builder()
                .eventId(UUID.randomUUID())
                .tournamentUuid(tournament.getUuid())
                .tournamentName(tournament.getName())
                .previousStatus(previousStatus)
                .newStatus(targetStatus)
                .changedBy(triggeredBy)
                .reason(reason)
                .occurredAt(Instant.now())
                .build();

        eventPublisher.publishEvent(event);

        // 5. Invoke post-transition handlers (registered per transition key)
        List<TransitionHandler> handlers = handlerRegistry.getOrDefault(key, List.of());
        for (TransitionHandler handler : handlers) {
            try {
                handler.onTransition(context);
            } catch (Exception ex) {
                // Handlers must not fail the transaction — log and continue
                log.error("[LIFECYCLE] Handler {} failed for transition {} → {}: {}",
                        handler.getClass().getSimpleName(), previousStatus, targetStatus, ex.getMessage(), ex);
            }
        }
    }

    /**
     * Convenience overload for system-triggered transitions (schedulers, event listeners).
     */
    @Transactional
    public void systemTransition(UUID tournamentUuid, TournamentStatus targetStatus, String reason) {
        transition(tournamentUuid, targetStatus, "SYSTEM", reason);
    }
}
