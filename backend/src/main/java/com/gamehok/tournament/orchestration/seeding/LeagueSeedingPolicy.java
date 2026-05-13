package com.gamehok.tournament.orchestration.seeding;

import com.gamehok.tournament.tournament.entity.TournamentParticipant;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * League/group-stage seeding policy — distributes tiers evenly across groups.
 *
 * <p>Goal: every group contains roughly equal numbers of STRONG, MODERATE, and WEAK
 * participants, preventing a "group of death" where all strong teams cluster together.
 *
 * <p>Algorithm (snake draft distribution):
 * <pre>
 * Groups: G1, G2, G3, G4 (for example)
 *
 * Round 1 (STRONG):  G1 gets STRONG-1, G2 gets STRONG-2, G3 gets STRONG-3, G4 gets STRONG-4
 * Round 2 (STRONG):  G4 gets STRONG-5, G3 gets STRONG-6 ... (snake reversal)
 * Round 3 (MODERATE): G1 gets MODERATE-1, G2 gets MODERATE-2 ...
 * ... until all participants are assigned.
 * </pre>
 *
 * <p>Snake draft ensures that if STRONG count is not divisible by groupCount,
 * the distribution is as equal as possible — no group gets 2 extra strong teams.
 */
@Component("leagueSeedingPolicy")
public class LeagueSeedingPolicy implements SeedingPolicy {

    @Override
    public List<TournamentParticipant> applySeedingOrder(
            List<CompetitiveAssessment> assessments,
            int groupCount
    ) {
        if (groupCount <= 1) {
            // No groups; straight sequential seeding
            return assignSequentialSeeds(assessments);
        }

        // Separate by tier
        Map<StrengthTier, List<CompetitiveAssessment>> byTier = assessments.stream()
                .collect(Collectors.groupingBy(CompetitiveAssessment::tier));

        List<CompetitiveAssessment> strong = new ArrayList<>(byTier.getOrDefault(StrengthTier.STRONG, List.of()));
        List<CompetitiveAssessment> moderate = new ArrayList<>(byTier.getOrDefault(StrengthTier.MODERATE, List.of()));
        List<CompetitiveAssessment> weak = new ArrayList<>(byTier.getOrDefault(StrengthTier.WEAK, List.of()));

        // Sort each tier by effective score descending
        Comparator<CompetitiveAssessment> byEffectiveScore =
                Comparator.comparingDouble(CompetitiveAssessment::effectiveScore).reversed();
        strong.sort(byEffectiveScore);
        moderate.sort(byEffectiveScore);
        weak.sort(byEffectiveScore);

        // Snake draft into groups: STRONG → MODERATE → WEAK
        List<List<TournamentParticipant>> groups = new ArrayList<>();
        for (int i = 0; i < groupCount; i++) {
            groups.add(new ArrayList<>());
        }

        snakeDraft(strong, groups);
        snakeDraft(moderate, groups);
        snakeDraft(weak, groups);

        // Flatten groups → assign seeds sequentially by group order
        List<TournamentParticipant> result = new ArrayList<>();
        int seedCounter = 1;
        for (List<TournamentParticipant> group : groups) {
            for (TournamentParticipant participant : group) {
                participant.setSeedNumber(seedCounter++);
                result.add(participant);
            }
        }

        return result;
    }

    /**
     * Performs a snake draft distribution of assessments into group buckets.
     * Alternates direction each round to ensure balance.
     */
    private void snakeDraft(List<CompetitiveAssessment> tier, List<List<TournamentParticipant>> groups) {
        int groupCount = groups.size();
        boolean forward = true;
        int groupIdx = 0;

        for (CompetitiveAssessment assessment : tier) {
            groups.get(groupIdx).add(assessment.participant());

            if (forward) {
                groupIdx++;
                if (groupIdx >= groupCount) {
                    groupIdx = groupCount - 1;
                    forward = false;
                }
            } else {
                groupIdx--;
                if (groupIdx < 0) {
                    groupIdx = 0;
                    forward = true;
                }
            }
        }
    }

    private List<TournamentParticipant> assignSequentialSeeds(List<CompetitiveAssessment> assessments) {
        List<TournamentParticipant> result = new ArrayList<>();
        for (int i = 0; i < assessments.size(); i++) {
            TournamentParticipant p = assessments.get(i).participant();
            p.setSeedNumber(i + 1);
            result.add(p);
        }
        return result;
    }
}
