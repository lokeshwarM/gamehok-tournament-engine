package com.gamehok.tournament.orchestration.seeding;

/**
 * Competitive strength tier assigned to a tournament participant.
 *
 * <p>Tiers are computed internally from the {@link CompetitiveRatingEngine}
 * and are NEVER exposed via any public API. They exist solely to drive
 * seeding and bracket placement decisions.
 *
 * <pre>
 * STRONG   — high confidence, proven track record, consistently top performers
 * MODERATE — solid history, some variance, competitive in mid-table positions
 * WEAK     — new/unproven participants, low data confidence, or poor track record
 * </pre>
 */
public enum StrengthTier {
    STRONG,
    MODERATE,
    WEAK
}
