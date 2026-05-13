package com.gamehok.tournament.enums;

/**
 * Represents a distinct phase or stage within a tournament.
 * <p>
 * Tournaments can be multi-stage, with each stage having its own format.
 * Example: GROUP_STAGE (league) → PLAYOFF (knockout) → GRAND_FINAL
 * </p>
 */
public enum StageType {

    GROUP_STAGE,
    ROUND_OF_128,
    ROUND_OF_64,
    ROUND_OF_32,
    ROUND_OF_16,
    QUARTER_FINAL,
    SEMI_FINAL,
    THIRD_PLACE_PLAYOFF,
    GRAND_FINAL,
    SWISS_ROUND,
    BATTLE_ROYALE_ROUND,
    WILDCARD,
    CUSTOM
}
