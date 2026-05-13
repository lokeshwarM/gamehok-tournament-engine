package com.gamehok.tournament.orchestration.lifecycle;

import com.gamehok.tournament.tournament.entity.Tournament;

/**
 * Side-effect handler executed AFTER a tournament status has been successfully persisted.
 *
 * <p>Each handler reacts to a specific transition (e.g., REGISTRATION_OPEN → REGISTRATION_CLOSED)
 * and performs one cohesive workflow step: publishing events, scheduling jobs, sending
 * notifications, triggering seeding, etc.
 *
 * <p>Implementations must be idempotent — they may be re-invoked on retry.
 * Implementations are Spring {@code @Component}s and should use
 * {@code @Async} where side effects can be deferred.
 */
public interface TransitionHandler {

    /**
     * Handles post-transition side effects for a given tournament and transition context.
     *
     * @param context the transition that just completed
     */
    void onTransition(TransitionContext context);
}
