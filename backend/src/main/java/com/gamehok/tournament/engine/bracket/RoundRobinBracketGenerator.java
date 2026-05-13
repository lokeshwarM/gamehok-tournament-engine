package com.gamehok.tournament.engine.bracket;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Round-robin (league) bracket generator.
 * <p>
 * Generates a complete round-robin schedule where every participant
 * plays against every other participant exactly once.
 * Total matches = N*(N-1)/2 for N participants.
 * Uses the circle method (polygon algorithm) for balanced scheduling.
 * </p>
 */
@Component("roundRobinGenerator")
public class RoundRobinBracketGenerator implements BracketGenerator {

    @Override
    public List<MatchSlot> generateBracket(List<Long> participantIds, Long stageId, int bestOf) {
        List<MatchSlot> slots = new ArrayList<>();
        int n = participantIds.size();
        List<Long> ids = new ArrayList<>(participantIds);

        // Add dummy "BYE" for odd participant count
        if (n % 2 != 0) {
            ids.add(null);
            n++;
        }

        int totalRounds = n - 1;
        int matchNumber = 1;

        for (int round = 1; round <= totalRounds; round++) {
            for (int i = 0; i < n / 2; i++) {
                Long p1 = ids.get(i);
                Long p2 = ids.get(n - 1 - i);
                boolean isBye = (p1 == null || p2 == null);

                if (!isBye) {
                    slots.add(new MatchSlot(
                            stageId, round, matchNumber, i + 1,
                            p1, p2, null, null, false, bestOf
                    ));
                    matchNumber++;
                }
            }

            // Rotate: fix first element, rotate rest clockwise
            Long last = ids.remove(n - 1);
            ids.add(1, last);
        }

        return slots;
    }
}
