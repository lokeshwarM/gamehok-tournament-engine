package com.gamehok.tournament.orchestration.lifecycle;

import com.gamehok.tournament.enums.TournamentStatus;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * Defines the valid state transitions for the tournament lifecycle.
 *
 * <p>This is the canonical source of truth for what transitions are legally allowed.
 * No transition should occur outside of this definition.
 *
 * <pre>
 * DRAFT
 *   └─► REGISTRATION_OPEN          (registration window opens)
 *   └─► CANCELLED                  (admin cancels before any registration)
 *
 * REGISTRATION_OPEN
 *   └─► REGISTRATION_CLOSED        (window expires: T-2h before start)
 *   └─► CANCELLED                  (admin force-cancels)
 *
 * REGISTRATION_CLOSED
 *   └─► CHECK_IN                   (after validation passes)
 *   └─► SEEDING                    (if no check-in phase configured)
 *   └─► CANCELLED                  (not enough participants)
 *
 * CHECK_IN
 *   └─► SEEDING                    (check-in window closes)
 *   └─► CANCELLED                  (too few checked in)
 *
 * SEEDING
 *   └─► IN_PROGRESS                (bracket generated, matches scheduled)
 *
 * IN_PROGRESS
 *   └─► PAUSED                     (admin suspends)
 *   └─► COMPLETED                  (all matches finished, final rank assigned)
 *
 * PAUSED
 *   └─► IN_PROGRESS                (admin resumes)
 *   └─► CANCELLED                  (admin cancels mid-tournament)
 *
 * COMPLETED
 *   └─► ARCHIVED                   (post-processing done)
 *
 * CANCELLED
 *   └─► ARCHIVED                   (cleanup complete)
 * </pre>
 */
public final class TournamentLifecycleTransitions {

    private static final Map<TournamentStatus, Set<TournamentStatus>> ALLOWED_TRANSITIONS =
            new EnumMap<>(TournamentStatus.class);

    static {
        ALLOWED_TRANSITIONS.put(TournamentStatus.DRAFT, EnumSet.of(
                TournamentStatus.REGISTRATION_OPEN,
                TournamentStatus.CANCELLED
        ));
        ALLOWED_TRANSITIONS.put(TournamentStatus.REGISTRATION_OPEN, EnumSet.of(
                TournamentStatus.REGISTRATION_CLOSED,
                TournamentStatus.CANCELLED
        ));
        ALLOWED_TRANSITIONS.put(TournamentStatus.REGISTRATION_CLOSED, EnumSet.of(
                TournamentStatus.CHECK_IN,
                TournamentStatus.SEEDING,
                TournamentStatus.CANCELLED
        ));
        ALLOWED_TRANSITIONS.put(TournamentStatus.CHECK_IN, EnumSet.of(
                TournamentStatus.SEEDING,
                TournamentStatus.CANCELLED
        ));
        ALLOWED_TRANSITIONS.put(TournamentStatus.SEEDING, EnumSet.of(
                TournamentStatus.IN_PROGRESS
        ));
        ALLOWED_TRANSITIONS.put(TournamentStatus.IN_PROGRESS, EnumSet.of(
                TournamentStatus.PAUSED,
                TournamentStatus.COMPLETED
        ));
        ALLOWED_TRANSITIONS.put(TournamentStatus.PAUSED, EnumSet.of(
                TournamentStatus.IN_PROGRESS,
                TournamentStatus.CANCELLED
        ));
        ALLOWED_TRANSITIONS.put(TournamentStatus.COMPLETED, EnumSet.of(
                TournamentStatus.ARCHIVED
        ));
        ALLOWED_TRANSITIONS.put(TournamentStatus.CANCELLED, EnumSet.of(
                TournamentStatus.ARCHIVED
        ));
        ALLOWED_TRANSITIONS.put(TournamentStatus.ARCHIVED, EnumSet.noneOf(TournamentStatus.class));
    }

    private TournamentLifecycleTransitions() {}

    /**
     * Returns whether a transition from {@code from} to {@code to} is valid.
     */
    public static boolean isAllowed(TournamentStatus from, TournamentStatus to) {
        Set<TournamentStatus> reachable = ALLOWED_TRANSITIONS.getOrDefault(from, EnumSet.noneOf(TournamentStatus.class));
        return reachable.contains(to);
    }

    /**
     * Returns all valid next states from the given status.
     */
    public static Set<TournamentStatus> allowedTransitions(TournamentStatus from) {
        Set<TournamentStatus> reachable = ALLOWED_TRANSITIONS.getOrDefault(from, EnumSet.noneOf(TournamentStatus.class));
        return reachable.isEmpty() ? java.util.Collections.emptySet() : EnumSet.copyOf(reachable);
    }

    /**
     * Returns true if the given status is a terminal state (no further transitions possible).
     */
    public static boolean isTerminal(TournamentStatus status) {
        return ALLOWED_TRANSITIONS.getOrDefault(status, EnumSet.noneOf(TournamentStatus.class)).isEmpty();
    }
}
