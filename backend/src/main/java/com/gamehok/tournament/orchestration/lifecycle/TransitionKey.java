package com.gamehok.tournament.orchestration.lifecycle;

import com.gamehok.tournament.enums.TournamentStatus;

import java.util.Objects;

/**
 * Composite key identifying a specific (fromStatus → toStatus) transition.
 *
 * <p>Used as map keys in the guard and handler registries of the state machine.
 */
public record TransitionKey(TournamentStatus from, TournamentStatus to) {

    public static TransitionKey of(TournamentStatus from, TournamentStatus to) {
        return new TransitionKey(from, to);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransitionKey other)) return false;
        return from == other.from && to == other.to;
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }
}
