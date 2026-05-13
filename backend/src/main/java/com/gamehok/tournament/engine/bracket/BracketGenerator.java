package com.gamehok.tournament.engine.bracket;

import java.util.List;

/**
 * Strategy interface for bracket generation algorithms.
 * <p>
 * Implementations:
 * - {@link SingleEliminationBracketGenerator} - Single elimination
 * - {@link DoubleEliminationBracketGenerator} - Double elimination (winners/losers bracket)
 * - {@link RoundRobinBracketGenerator} - Full round-robin league schedule
 * - {@link SwissBracketGenerator} - Swiss-system pairing
 * </p>
 */
public interface BracketGenerator {

    /**
     * Generates match slots for the given list of seeded participant IDs.
     *
     * @param participantIds ordered list of participant IDs (position = seed)
     * @param stageId        the stage this bracket belongs to
     * @param bestOf         best-of series configuration per match
     * @return list of match slot definitions ready to be persisted
     */
    List<MatchSlot> generateBracket(List<Long> participantIds, Long stageId, int bestOf);

    /**
     * Simple data class representing a generated match slot before persistence.
     */
    record MatchSlot(
            Long stageId,
            Integer roundNumber,
            Integer matchNumber,
            Integer bracketPosition,
            Long participant1Id,
            Long participant2Id,
            Long nextMatchId,
            Long loserNextMatchId,
            boolean bye,
            int bestOf
    ) {}
}
