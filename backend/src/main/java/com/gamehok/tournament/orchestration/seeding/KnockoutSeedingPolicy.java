package com.gamehok.tournament.orchestration.seeding;

import com.gamehok.tournament.tournament.entity.TournamentParticipant;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Knockout-format seeding policy using retention-aware placement.
 *
 * <p>Goal: ensure early rounds are competitively balanced while naturally increasing
 * match quality in later rounds. Prevents a WEAK team from meeting the STRONG
 * champion-candidate in round 1.
 *
 * <p>Algorithm:
 * <pre>
 * Given participants sorted by effective score (index 0 = strongest):
 *
 * Bracket positions:
 *   Top half = seeds 1, 3, 5, 7... (STRONG/top-MODERATE)
 *   Bottom half = seeds 2, 4, 6, 8... (WEAK/bottom-MODERATE)
 *
 * Round 1 matchups by seed:
 *   seed 1 vs seed N
 *   seed 2 vs seed N-1
 *   seed 3 vs seed N-2
 *   ...
 *
 * This guarantees:
 *   STRONG vs WEAK   (seed 1 vs seed N)
 *   MODERATE vs WEAK (seed 2 vs seed N-1)
 *   ... competitive balance escalates each round
 * </pre>
 *
 * <p>Deliberately does NOT match WEAK vs STRONG to prevent early eliminations of
 * potentially interesting underdogs while still providing clear paths for top seeds.
 */
@Component("knockoutSeedingPolicy")
public class KnockoutSeedingPolicy implements SeedingPolicy {

    @Override
    public List<TournamentParticipant> applySeedingOrder(
            List<CompetitiveAssessment> assessments,
            int groupCount
    ) {
        // Sort: STRONG first, then MODERATE, then WEAK; within tier by effective score
        List<CompetitiveAssessment> sorted = new ArrayList<>(assessments);
        sorted.sort((a, b) -> {
            int tierCompare = tierRank(a.tier()) - tierRank(b.tier());
            if (tierCompare != 0) return tierCompare;
            return Double.compare(b.effectiveScore(), a.effectiveScore());
        });

        List<TournamentParticipant> result = new ArrayList<>();

        // Assign seed numbers 1..N
        for (int i = 0; i < sorted.size(); i++) {
            TournamentParticipant participant = sorted.get(i).participant();
            participant.setSeedNumber(i + 1);
            result.add(participant);
        }

        return result;
    }

    /**
     * Returns sort priority for tier (lower = placed earlier in bracket = top seeded).
     */
    private int tierRank(StrengthTier tier) {
        return switch (tier) {
            case STRONG -> 0;
            case MODERATE -> 1;
            case WEAK -> 2;
        };
    }
}
