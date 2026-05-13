package com.gamehok.tournament.engine.progression;

import com.gamehok.tournament.match.entity.Match;

/**
 * Handles progression of match winners/losers to the next bracket position.
 * <p>
 * After a match completes, this component:
 * 1. Identifies the next match slot for the winner
 * 2. Places the winner into that slot (participant1 or participant2)
 * 3. For double elimination: places the loser into the losers' bracket
 * 4. Checks if the current round/stage is complete
 * 5. Triggers stage completion if all matches in the stage are done
 * </p>
 */
public interface ProgressionEngine {

    /**
     * Processes bracket advancement after a match result is finalized.
     *
     * @param completedMatch the match that just completed
     */
    void advanceParticipants(Match completedMatch);

    /**
     * Checks if the given stage has all matches completed.
     *
     * @param stageId the stage to check
     * @return true if stage is complete
     */
    boolean isStageComplete(Long stageId);

    /**
     * Triggers qualification logic to determine which participants advance to the next stage.
     *
     * @param stageId the completed stage
     */
    void handleStageCompletion(Long stageId);
}
