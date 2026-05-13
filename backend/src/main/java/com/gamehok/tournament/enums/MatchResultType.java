package com.gamehok.tournament.enums;

/**
 * Describes how the result of a match was determined.
 */
public enum MatchResultType {

    SCORE,           // Result decided by score/kills/points
    WALKOVER,        // Opponent did not show up / forfeited
    DISQUALIFICATION,// Opponent was disqualified
    DRAW,            // Match ended in a draw
    ADMIN_OVERRIDE,  // Admin manually set the result
    SYSTEM_AUTO      // System automatically resolved (e.g., timeout)
}
