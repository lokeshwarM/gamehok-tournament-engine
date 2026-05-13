package com.gamehok.tournament.orchestration.lifecycle;

import com.gamehok.tournament.enums.TournamentStatus;
import com.gamehok.tournament.orchestration.lifecycle.handler.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Wires all {@link TransitionHandler} implementations to their respective transitions.
 *
 * <p>Handlers are executed sequentially after a successful state commit.
 * Exceptions from handlers are caught and logged — they do NOT roll back the transition.
 */
@Component
public class TournamentLifecycleHandlerRegistry {

    private final RegistrationClosedHandler registrationClosedHandler;
    private final SeedingPreparationHandler seedingPreparationHandler;
    private final TournamentStartHandler tournamentStartHandler;
    private final TournamentCompletionHandler tournamentCompletionHandler;

    public TournamentLifecycleHandlerRegistry(
            RegistrationClosedHandler registrationClosedHandler,
            SeedingPreparationHandler seedingPreparationHandler,
            TournamentStartHandler tournamentStartHandler,
            TournamentCompletionHandler tournamentCompletionHandler
    ) {
        this.registrationClosedHandler = registrationClosedHandler;
        this.seedingPreparationHandler = seedingPreparationHandler;
        this.tournamentStartHandler = tournamentStartHandler;
        this.tournamentCompletionHandler = tournamentCompletionHandler;
    }

    /**
     * Builds the full handler registry.
     *
     * <p>Mapping: {@code TransitionKey(from, to) → [ordered handlers]}.
     */
    public Map<TransitionKey, List<TransitionHandler>> buildHandlerMap() {
        Map<TransitionKey, List<TransitionHandler>> map = new HashMap<>();

        // When registration closes: validate teams, publish notifications
        map.put(
                TransitionKey.of(TournamentStatus.REGISTRATION_OPEN, TournamentStatus.REGISTRATION_CLOSED),
                List.of(registrationClosedHandler)
        );

        // When seeding begins (from either REGISTRATION_CLOSED or CHECK_IN):
        map.put(
                TransitionKey.of(TournamentStatus.REGISTRATION_CLOSED, TournamentStatus.SEEDING),
                List.of(seedingPreparationHandler)
        );
        map.put(
                TransitionKey.of(TournamentStatus.CHECK_IN, TournamentStatus.SEEDING),
                List.of(seedingPreparationHandler)
        );

        // When tournament goes IN_PROGRESS: generate brackets, schedule matches
        map.put(
                TransitionKey.of(TournamentStatus.SEEDING, TournamentStatus.IN_PROGRESS),
                List.of(tournamentStartHandler)
        );

        // When tournament completes: distribute prizes, update leaderboards, archive
        map.put(
                TransitionKey.of(TournamentStatus.IN_PROGRESS, TournamentStatus.COMPLETED),
                List.of(tournamentCompletionHandler)
        );

        return map;
    }
}
