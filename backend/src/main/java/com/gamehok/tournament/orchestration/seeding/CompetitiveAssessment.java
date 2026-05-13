package com.gamehok.tournament.orchestration.seeding;

import com.gamehok.tournament.tournament.entity.TournamentParticipant;

/**
 * Internal-only model carrying a participant's competitive assessment.
 *
 * <p>This record is NEVER serialized to JSON or exposed via any API endpoint.
 * It lives purely within the seeding pipeline as a transient computation.
 *
 * @param participant     the tournament participant
 * @param compositeScore  normalized composite score [0.0, 100.0] — higher = stronger
 * @param confidenceScore data confidence [0.0, 1.0] — lower = fewer data points
 * @param tier            categorized strength tier
 */
public record CompetitiveAssessment(
        TournamentParticipant participant,
        double compositeScore,
        double confidenceScore,
        StrengthTier tier
) {
    /**
     * Effective score used for sorting: penalizes low-confidence assessments.
     * New/unproven teams are ranked lower when confidence is low.
     */
    public double effectiveScore() {
        return compositeScore * confidenceScore;
    }
}
