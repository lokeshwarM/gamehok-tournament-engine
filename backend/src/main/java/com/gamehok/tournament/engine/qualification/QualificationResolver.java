package com.gamehok.tournament.engine.qualification;

import com.gamehok.tournament.tournament.entity.TournamentParticipant;

import java.util.List;

/**
 * Qualification logic interface determining which participants advance between stages.
 * <p>
 * For LEAGUE stages: top N by points (ties broken by W/GD/kills)
 * For KNOCKOUT stages: match winners
 * For HYBRID: group winners + best runners-up
 * For SWISS: configurable threshold after all rounds
 * </p>
 */
public interface QualificationResolver {

    /**
     * Determines which participants qualify from a completed stage.
     *
     * @param stageId       the completed stage
     * @param qualifierCount number of participants to advance
     * @return ordered list of qualifying participants (position matters for next stage seeding)
     */
    List<TournamentParticipant> resolveQualifiers(Long stageId, int qualifierCount);
}
