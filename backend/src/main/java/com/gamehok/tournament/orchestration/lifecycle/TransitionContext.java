package com.gamehok.tournament.orchestration.lifecycle;

import com.gamehok.tournament.enums.TournamentStatus;
import com.gamehok.tournament.tournament.entity.Tournament;

/**
 * Context object passed to transition guards and side-effect handlers.
 *
 * <p>Carries all information needed for a lifecycle transition decision:
 * the tournament being transitioned, the intended new status, and
 * optional contextual metadata (who triggered it, why).
 *
 * <p>Immutable by design — once constructed it is read-only within the guard chain.
 */
public record TransitionContext(
        Tournament tournament,
        TournamentStatus targetStatus,
        String triggeredBy,
        String reason
) {
    /**
     * Convenience factory for system-triggered transitions (scheduled jobs, event listeners).
     */
    public static TransitionContext system(Tournament tournament, TournamentStatus target, String reason) {
        return new TransitionContext(tournament, target, "SYSTEM", reason);
    }

    /**
     * Convenience factory for admin-triggered transitions.
     */
    public static TransitionContext admin(Tournament tournament, TournamentStatus target, String adminPrincipal) {
        return new TransitionContext(tournament, target, adminPrincipal, "Admin-initiated");
    }
}
