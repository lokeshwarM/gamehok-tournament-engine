package com.gamehok.tournament.engine.bracket;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Single elimination bracket generator.
 * <p>
 * Generates a single-elimination bracket where the total number of matches
 * is always (N - 1) for N participants. Handles non-power-of-2 participant
 * counts by inserting BYE matches in the first round.
 * <p>
 * Top seed faces lowest seed, second seed faces second-lowest, etc.
 * Winner of each match is linked to the correct next-round match slot.
 * </p>
 */
@Component("singleEliminationGenerator")
public class SingleEliminationBracketGenerator implements BracketGenerator {

    @Override
    public List<MatchSlot> generateBracket(List<Long> participantIds, Long stageId, int bestOf) {
        List<MatchSlot> slots = new ArrayList<>();
        int n = participantIds.size();
        int size = nextPowerOfTwo(n);
        int totalRounds = (int) (Math.log(size) / Math.log(2));

        // Pad with nulls (BYE) for non-power-of-2 counts
        List<Long> padded = new ArrayList<>(participantIds);
        while (padded.size() < size) {
            padded.add(null);
        }

        // Generate round 1 matches
        int matchNumber = 1;
        List<Long> round1MatchIds = new ArrayList<>();
        for (int i = 0; i < size / 2; i++) {
            Long p1 = padded.get(i);
            Long p2 = padded.get(size - 1 - i);
            boolean isBye = (p1 == null || p2 == null);

            slots.add(new MatchSlot(
                    stageId, 1, matchNumber, i + 1,
                    p1, p2, null, null, isBye, bestOf
            ));
            matchNumber++;
        }

        // Generate subsequent rounds as empty slots (participants filled after advancement)
        for (int round = 2; round <= totalRounds; round++) {
            int matchesInRound = size / (int) Math.pow(2, round);
            for (int i = 0; i < matchesInRound; i++) {
                slots.add(new MatchSlot(
                        stageId, round, matchNumber, i + 1,
                        null, null, null, null, false, bestOf
                ));
                matchNumber++;
            }
        }

        return slots;
    }

    private int nextPowerOfTwo(int n) {
        int power = 1;
        while (power < n) power *= 2;
        return power;
    }
}
