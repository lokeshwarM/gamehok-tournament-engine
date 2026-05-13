package com.gamehok.tournament.engine.matchmaking;

import com.gamehok.tournament.matchmaking.entity.MatchmakingQueue;

import java.util.List;
import java.util.Optional;

/**
 * ELO-based matchmaking algorithm interface.
 * <p>
 * Finds the best opponent for a queued player within their ELO window.
 * Window expands over time to prevent indefinite waiting.
 * </p>
 */
public interface MatchmakingAlgorithm {

    /**
     * Finds a suitable opponent for the given queue entry.
     *
     * @param entry all currently searching queue entries
     * @param candidate the entry to find an opponent for
     * @return optional matching opponent, empty if no match found yet
     */
    Optional<MatchmakingQueue> findOpponent(List<MatchmakingQueue> entry, MatchmakingQueue candidate);

    /**
     * Expands the ELO window for entries that have been waiting too long.
     *
     * @param entries queue entries to update
     * @param expansionAmount ELO points to expand window by
     */
    void expandEloWindows(List<MatchmakingQueue> entries, int expansionAmount);
}
