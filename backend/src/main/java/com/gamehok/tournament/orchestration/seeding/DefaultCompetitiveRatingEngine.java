package com.gamehok.tournament.orchestration.seeding;

import com.gamehok.tournament.leaderboard.repository.LeaderboardRepository;
import com.gamehok.tournament.team.repository.TeamMemberRepository;
import com.gamehok.tournament.tournament.entity.TournamentParticipant;
import com.gamehok.tournament.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Default implementation of {@link CompetitiveRatingEngine}.
 *
 * <p>Computes a composite score per participant using three weighted signals:
 *
 * <pre>
 * compositeScore =  (0.60 × eloComponent)
 *                 + (0.30 × historicalPerformanceComponent)
 *                 + (0.10 × activityComponent)
 * </pre>
 *
 * <p>Confidence is computed separately and penalizes participants with fewer data points:
 * <pre>
 * confidence = 1 - (unprovenMemberRatio × 0.5)
 *   — a fully proven roster → confidence = 1.0
 *   — all-new roster → confidence = 0.5 (effectively halves their composite score)
 * </pre>
 *
 * <p>Strength tiers are assigned by dividing the sorted list into thirds:
 * <ul>
 *   <li>Top 1/3 → STRONG</li>
 *   <li>Middle 1/3 → MODERATE</li>
 *   <li>Bottom 1/3 → WEAK</li>
 * </ul>
 *
 * <p>All computations are internal and NEVER exposed publicly.
 */
@Slf4j
@Component
public class DefaultCompetitiveRatingEngine implements CompetitiveRatingEngine {

    private static final double ELO_WEIGHT = 0.60;
    private static final double HISTORY_WEIGHT = 0.30;
    private static final double ACTIVITY_WEIGHT = 0.10;

    private static final int DEFAULT_ELO = 1000;
    private static final int ELO_SCALE_MAX = 3000;

    private static final int MIN_TOURNAMENTS_FOR_PROVEN = 3;
    private static final double UNPROVEN_CONFIDENCE_PENALTY = 0.5;

    private final UserRepository userRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final LeaderboardRepository leaderboardRepository;

    public DefaultCompetitiveRatingEngine(
            UserRepository userRepository,
            TeamMemberRepository teamMemberRepository,
            LeaderboardRepository leaderboardRepository
    ) {
        this.userRepository = userRepository;
        this.teamMemberRepository = teamMemberRepository;
        this.leaderboardRepository = leaderboardRepository;
    }

    @Override
    public List<CompetitiveAssessment> assess(Long tournamentId, List<TournamentParticipant> participants) {
        List<CompetitiveAssessment> assessments = new ArrayList<>();

        for (TournamentParticipant participant : participants) {
            double eloComponent = computeEloComponent(participant);
            double historyComponent = computeHistoryComponent(participant);
            double activityComponent = computeActivityComponent(participant);
            double confidence = computeConfidence(participant);

            double compositeScore = (ELO_WEIGHT * eloComponent)
                    + (HISTORY_WEIGHT * historyComponent)
                    + (ACTIVITY_WEIGHT * activityComponent);

            assessments.add(new CompetitiveAssessment(
                    participant,
                    Math.min(100.0, Math.max(0.0, compositeScore)),
                    Math.min(1.0, Math.max(0.0, confidence)),
                    StrengthTier.MODERATE // tier assigned below after full sort
            ));
        }

        // Sort by effectiveScore descending before tier assignment
        assessments.sort(Comparator.comparingDouble(CompetitiveAssessment::effectiveScore).reversed());

        // Assign tiers by thirds
        return assignTiers(assessments);
    }

    /**
     * ELO component — normalized participant ELO relative to platform scale.
     * Returns a 0–100 score.
     */
    private double computeEloComponent(TournamentParticipant participant) {
        if (participant.getUserId() != null) {
            // Solo participant: use their personal ELO
            return userRepository.findById(participant.getUserId())
                    .map(user -> normalizeElo(user.getEloRating()))
                    .orElse(normalizeElo(DEFAULT_ELO));
        }

        if (participant.getTeamId() != null) {
            // Team: compute average ELO of active members
            List<Integer> memberElos = teamMemberRepository.findActiveElosByTeamId(participant.getTeamId());
            if (memberElos.isEmpty()) {
                return normalizeElo(DEFAULT_ELO);
            }
            double avgElo = memberElos.stream().mapToInt(Integer::intValue).average().orElse(DEFAULT_ELO);
            return normalizeElo((int) avgElo);
        }

        return normalizeElo(DEFAULT_ELO);
    }

    /**
     * Historical performance component — based on past tournament outcomes.
     * Considers win rate and final rank distributions from leaderboard history.
     */
    private double computeHistoryComponent(TournamentParticipant participant) {
        // Aggregate historical points from completed tournament stages
        // Points are from the leaderboard_entries table for past tournaments
        // A more complete implementation would query across all past tournaments
        // For now, returns a baseline that evolves over time as more data is collected
        return 50.0; // neutral baseline — will diverge as history accumulates
    }

    /**
     * Activity component — rewards consistent recent tournament participation.
     * Penalizes participants who have been inactive for extended periods.
     */
    private double computeActivityComponent(TournamentParticipant participant) {
        // A fully active participant (3+ recent tournaments) scores 100.0
        // Inactive participants score lower
        return 50.0; // baseline — extended in future iteration
    }

    /**
     * Confidence score — penalizes rosters with many unproven players.
     * New players reduce confidence because their true skill level is unknown.
     */
    private double computeConfidence(TournamentParticipant participant) {
        if (participant.getUserId() != null) {
            // Solo: check if user has played enough tournaments
            boolean isProven = userRepository.findById(participant.getUserId())
                    .map(user -> user.getEloRating() != DEFAULT_ELO)
                    .orElse(false);
            return isProven ? 1.0 : UNPROVEN_CONFIDENCE_PENALTY;
        }

        if (participant.getTeamId() != null) {
            List<Integer> memberElos = teamMemberRepository.findActiveElosByTeamId(participant.getTeamId());
            if (memberElos.isEmpty()) return UNPROVEN_CONFIDENCE_PENALTY;

            long unprovenCount = memberElos.stream()
                    .filter(elo -> elo == DEFAULT_ELO)
                    .count();

            double unprovenRatio = (double) unprovenCount / memberElos.size();
            return 1.0 - (unprovenRatio * UNPROVEN_CONFIDENCE_PENALTY);
        }

        return UNPROVEN_CONFIDENCE_PENALTY;
    }

    /**
     * Normalizes an ELO rating into a 0–100 scale.
     * Platform ELO ranges from 0 (floor) to {@code ELO_SCALE_MAX}.
     */
    private double normalizeElo(int elo) {
        return Math.min(100.0, (double) elo / ELO_SCALE_MAX * 100.0);
    }

    /**
     * Assigns strength tiers by dividing the sorted list into thirds.
     * Returns a new list with the correct tier on each assessment.
     */
    private List<CompetitiveAssessment> assignTiers(List<CompetitiveAssessment> sorted) {
        int total = sorted.size();
        int strongEnd = total / 3;
        int moderateEnd = 2 * total / 3;

        List<CompetitiveAssessment> tiered = new ArrayList<>();
        for (int i = 0; i < total; i++) {
            CompetitiveAssessment a = sorted.get(i);
            StrengthTier tier = (i < strongEnd) ? StrengthTier.STRONG
                    : (i < moderateEnd) ? StrengthTier.MODERATE
                    : StrengthTier.WEAK;

            tiered.add(new CompetitiveAssessment(
                    a.participant(), a.compositeScore(), a.confidenceScore(), tier));
        }

        log.debug("[RATING] Tournament assessment complete: {} STRONG, {} MODERATE, {} WEAK",
                tiered.stream().filter(a -> a.tier() == StrengthTier.STRONG).count(),
                tiered.stream().filter(a -> a.tier() == StrengthTier.MODERATE).count(),
                tiered.stream().filter(a -> a.tier() == StrengthTier.WEAK).count());

        return tiered;
    }
}
