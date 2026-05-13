package com.gamehok.tournament.orchestration.lifecycle;

import com.gamehok.tournament.enums.TournamentStatus;
import com.gamehok.tournament.orchestration.lifecycle.guard.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Wires all {@link TransitionGuard} implementations to their respective transitions.
 *
 * <p>Guards are evaluated in order. The first failing guard aborts the transition.
 * Add new guards here when introducing new business rules for state transitions.
 */
@Component
public class TournamentLifecycleGuardRegistry {

    private final MinimumParticipantGuard minimumParticipantGuard;
    private final TeamSizeValidationGuard teamSizeValidationGuard;
    private final RegistrationWindowGuard registrationWindowGuard;
    private final MinimumCheckedInGuard minimumCheckedInGuard;

    public TournamentLifecycleGuardRegistry(
            MinimumParticipantGuard minimumParticipantGuard,
            TeamSizeValidationGuard teamSizeValidationGuard,
            RegistrationWindowGuard registrationWindowGuard,
            MinimumCheckedInGuard minimumCheckedInGuard
    ) {
        this.minimumParticipantGuard = minimumParticipantGuard;
        this.teamSizeValidationGuard = teamSizeValidationGuard;
        this.registrationWindowGuard = registrationWindowGuard;
        this.minimumCheckedInGuard = minimumCheckedInGuard;
    }

    /**
     * Builds the full guard registry.
     *
     * <p>Mapping: {@code TransitionKey(from, to) → [ordered guards]}.
     */
    public Map<TransitionKey, List<TransitionGuard>> buildGuardMap() {
        Map<TransitionKey, List<TransitionGuard>> map = new HashMap<>();

        // DRAFT → REGISTRATION_OPEN: registration window must be valid
        map.put(
                TransitionKey.of(TournamentStatus.DRAFT, TournamentStatus.REGISTRATION_OPEN),
                List.of(registrationWindowGuard)
        );

        // REGISTRATION_OPEN → REGISTRATION_CLOSED: no pre-conditions (time-driven)
        map.put(
                TransitionKey.of(TournamentStatus.REGISTRATION_OPEN, TournamentStatus.REGISTRATION_CLOSED),
                List.of()
        );

        // REGISTRATION_CLOSED → CHECK_IN: must have enough validated participants
        map.put(
                TransitionKey.of(TournamentStatus.REGISTRATION_CLOSED, TournamentStatus.CHECK_IN),
                List.of(minimumParticipantGuard, teamSizeValidationGuard)
        );

        // REGISTRATION_CLOSED → SEEDING: skip check-in path
        map.put(
                TransitionKey.of(TournamentStatus.REGISTRATION_CLOSED, TournamentStatus.SEEDING),
                List.of(minimumParticipantGuard, teamSizeValidationGuard)
        );

        // CHECK_IN → SEEDING: check-in threshold must be met
        map.put(
                TransitionKey.of(TournamentStatus.CHECK_IN, TournamentStatus.SEEDING),
                List.of(minimumCheckedInGuard)
        );

        // SEEDING → IN_PROGRESS: no guard needed; seeding service validates internally
        map.put(
                TransitionKey.of(TournamentStatus.SEEDING, TournamentStatus.IN_PROGRESS),
                List.of()
        );

        // All other transitions are structurally valid — no additional guards
        return map;
    }
}
