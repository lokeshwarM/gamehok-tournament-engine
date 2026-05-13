package com.gamehok.tournament.orchestration.service;

import java.util.UUID;

/**
 * Tournament Orchestration Engine — the central coordinator.
 * <p>
 * Manages the full tournament lifecycle from seeding through bracket generation,
 * match scheduling, stage progression, and prize distribution.
 * <p>
 * This is NOT a CRUD service. It orchestrates the cross-module workflow:
 * 1. SEEDING → calls SeedingStrategy
 * 2. BRACKET GENERATION → calls BracketGenerator (format-aware)
 * 3. MATCH SCHEDULING → calls MatchScheduler
 * 4. PROGRESSION → calls ProgressionEngine after each match
 * 5. STAGE TRANSITION → calls QualificationResolver + next stage setup
 * 6. COMPLETION → calls LeaderboardService + PrizeService
 * </p>
 */
public interface TournamentOrchestrationService {

    /**
     * Initiates seeding for registered participants and generates the first stage bracket.
     * Transitions tournament status: REGISTRATION_CLOSED → SEEDING → IN_PROGRESS
     */
    void seedAndGenerateBracket(UUID tournamentUuid);

    /**
     * Advances a tournament to the next stage after the current stage completes.
     * Handles qualification logic and generates the next stage bracket.
     */
    void advanceToNextStage(UUID tournamentUuid, Long completedStageId);

    /**
     * Called when a match result is finalized.
     * Updates leaderboard, advances bracket, and triggers stage completion if applicable.
     */
    void processMatchCompletion(UUID matchUuid);

    /**
     * Finalizes the tournament: computes final rankings, distributes prizes, sends notifications.
     */
    void completeTournament(UUID tournamentUuid);

    /**
     * Handles timeout scenarios: auto-cancels unplayed matches, assigns walkovers.
     */
    void handleMatchTimeout(UUID matchUuid);
}
