package com.gamehok.tournament.orchestration.lifecycle;

/**
 * A guard (pre-condition) that must be satisfied before a lifecycle transition proceeds.
 *
 * <p>Guards are composable and form a chain-of-responsibility. Each guard is responsible
 * for exactly one business invariant. If a guard fails, the transition is aborted and
 * a {@link com.gamehok.tournament.common.exception.InvalidStateTransitionException} is thrown.
 *
 * <p>Guards are checked BEFORE any state is mutated.
 *
 * <p>Implementations should be stateless Spring {@code @Component}s.
 *
 * <p>Examples of guards:
 * <ul>
 *   <li>MinimumParticipantGuard — ensures enough teams registered</li>
 *   <li>RegistrationWindowGuard — ensures registration hasn't already passed</li>
 *   <li>TeamSizeValidationGuard — ensures all registered teams have correct size</li>
 * </ul>
 */
@FunctionalInterface
public interface TransitionGuard {

    /**
     * Validates that the transition described by {@code context} may proceed.
     *
     * @param context the full context of the attempted transition
     * @throws com.gamehok.tournament.common.exception.InvalidStateTransitionException
     *         if the guard condition is not satisfied
     */
    void validate(TransitionContext context);
}
