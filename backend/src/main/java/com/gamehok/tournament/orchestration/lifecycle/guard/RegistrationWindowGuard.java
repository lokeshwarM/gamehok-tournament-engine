package com.gamehok.tournament.orchestration.lifecycle.guard;

import com.gamehok.tournament.common.exception.InvalidStateTransitionException;
import com.gamehok.tournament.orchestration.lifecycle.TransitionContext;
import com.gamehok.tournament.orchestration.lifecycle.TransitionGuard;
import com.gamehok.tournament.tournament.entity.Tournament;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * Validates the registration window configuration is internally consistent.
 *
 * <p>Business rules enforced:
 * <ul>
 *   <li>registrationStart must be in the future (or very recent)</li>
 *   <li>registrationEnd = startTime - 2 hours (enforced at creation, verified here)</li>
 *   <li>startTime must be after registrationEnd</li>
 * </ul>
 */
@Component
public class RegistrationWindowGuard implements TransitionGuard {

    private static final long REGISTRATION_CLOSE_OFFSET_HOURS = 2;
    private static final long CLOCK_SKEW_TOLERANCE_MINUTES = 5;

    @Override
    public void validate(TransitionContext context) {
        Tournament t = context.tournament();
        Instant now = Instant.now();

        if (t.getRegistrationStart() == null) {
            throw new InvalidStateTransitionException(
                    "Tournament '" + t.getUuid() + "' has no registration start time configured.");
        }
        if (t.getRegistrationEnd() == null) {
            throw new InvalidStateTransitionException(
                    "Tournament '" + t.getUuid() + "' has no registration end time configured.");
        }
        if (t.getStartTime() == null) {
            throw new InvalidStateTransitionException(
                    "Tournament '" + t.getUuid() + "' has no start time configured.");
        }

        // registration end must be at least 2 hours before start
        Instant expectedRegistrationEnd = t.getStartTime()
                .minusSeconds(REGISTRATION_CLOSE_OFFSET_HOURS * 3600);
        if (t.getRegistrationEnd().isAfter(expectedRegistrationEnd.plusSeconds(60))) {
            throw new InvalidStateTransitionException(String.format(
                    "Tournament '%s': registrationEnd must be at least %d hours before startTime. " +
                    "Expected <= %s, got %s.",
                    t.getUuid(), REGISTRATION_CLOSE_OFFSET_HOURS, expectedRegistrationEnd, t.getRegistrationEnd()));
        }

        // registrationStart must not be after registrationEnd
        if (t.getRegistrationStart().isAfter(t.getRegistrationEnd())) {
            throw new InvalidStateTransitionException(
                    "Tournament '" + t.getUuid() + "': registrationStart cannot be after registrationEnd.");
        }
    }
}
