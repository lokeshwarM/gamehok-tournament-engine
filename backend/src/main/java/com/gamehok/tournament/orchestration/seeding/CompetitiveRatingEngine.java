package com.gamehok.tournament.orchestration.seeding;

import com.gamehok.tournament.tournament.entity.TournamentParticipant;

import java.util.List;

/**
 * Strategy contract for computing competitive assessments for all tournament participants.
 *
 * <p>Implementations compute a numeric composite score and confidence level for each
 * participant based on historical performance, ELO, and roster data.
 *
 * <p>Critical invariant: ratings are INTERNAL ONLY. No implementation may expose
 * raw scores or tiers through any public interface.
 */
public interface CompetitiveRatingEngine {

    /**
     * Computes competitive assessments for all participants in a given tournament.
     *
     * @param tournamentId  the tournament being seeded
     * @param participants  all registered participants for this tournament
     * @return list of assessments in descending order of effective score (strongest first)
     */
    List<CompetitiveAssessment> assess(Long tournamentId, List<TournamentParticipant> participants);
}
