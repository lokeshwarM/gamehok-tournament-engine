package com.gamehok.tournament.enums;

/**
 * Defines the structural format of a tournament.
 * <p>
 * - KNOCKOUT: Single/double elimination bracket; losers are eliminated.
 * - LEAGUE: Round-robin format; teams accumulate points over multiple matches.
 * - HYBRID: Combination of league group stage followed by knockout playoffs.
 * - BATTLE_ROYALE: Free-for-all format with multiple participants per match.
 * - SWISS: Swiss-system tournament; players paired by similar performance each round.
 * </p>
 */
public enum TournamentType {

    KNOCKOUT,
    LEAGUE,
    HYBRID,
    BATTLE_ROYALE,
    SWISS
}
