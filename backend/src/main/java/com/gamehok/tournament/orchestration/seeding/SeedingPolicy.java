package com.gamehok.tournament.orchestration.seeding;

import com.gamehok.tournament.tournament.entity.TournamentParticipant;

import java.util.List;

/**
 * Strategy contract for applying tournament-format-specific seeding policies.
 *
 * <p>A seeding policy takes the competitively assessed participants and assigns
 * final seed numbers. The result determines bracket position (KNOCKOUT)
 * or group assignment (LEAGUE/HYBRID).
 *
 * <p>Implementations:
 * <ul>
 *   <li>{@link KnockoutSeedingPolicy} — retention-aware: avoids WEAK vs STRONG in early rounds</li>
 *   <li>{@link LeagueSeedingPolicy} — balanced distribution across groups</li>
 * </ul>
 */
public interface SeedingPolicy {

    /**
     * Assigns seed numbers to participants based on their competitive assessments.
     *
     * <p>After this method returns, each participant's {@code seedNumber} field is set.
     * The seed number determines bracket position or group placement.
     *
     * @param assessments participants sorted by effective score (strongest first)
     * @param groupCount  number of groups/brackets to distribute into (1 for pure knockout)
     * @return participants in final seeded order (seed 1 = strongest)
     */
    List<TournamentParticipant> applySeedingOrder(List<CompetitiveAssessment> assessments, int groupCount);
}
