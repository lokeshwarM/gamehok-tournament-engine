package com.gamehok.tournament.orchestration.seeding;

import com.gamehok.tournament.enums.ParticipantStatus;
import com.gamehok.tournament.enums.TournamentType;
import com.gamehok.tournament.tournament.entity.Tournament;
import com.gamehok.tournament.tournament.entity.TournamentParticipant;
import com.gamehok.tournament.tournament.entity.TournamentStage;
import com.gamehok.tournament.tournament.repository.TournamentParticipantRepository;
import com.gamehok.tournament.tournament.repository.TournamentRepository;
import com.gamehok.tournament.tournament.repository.TournamentStageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Orchestrates the complete seeding pipeline for a tournament entering the SEEDING state.
 *
 * <p>Pipeline steps:
 * <ol>
 *   <li>Load all eligible participants (REGISTERED or CHECKED_IN)</li>
 *   <li>Compute competitive assessments via {@link CompetitiveRatingEngine}</li>
 *   <li>Select the appropriate {@link SeedingPolicy} based on tournament type</li>
 *   <li>Apply seeding order, assign seed numbers to participants</li>
 *   <li>Update participants to SEEDED status in a single batch</li>
 * </ol>
 *
 * <p>This service is invoked by {@link com.gamehok.tournament.orchestration.lifecycle.handler.SeedingPreparationHandler}
 * and is NOT accessible via any HTTP endpoint.
 */
@Slf4j
@Service
public class SeedingOrchestrationService {

    private final TournamentRepository tournamentRepository;
    private final TournamentParticipantRepository participantRepository;
    private final CompetitiveRatingEngine ratingEngine;
    private final KnockoutSeedingPolicy knockoutSeedingPolicy;
    private final LeagueSeedingPolicy leagueSeedingPolicy;

    public SeedingOrchestrationService(
            TournamentRepository tournamentRepository,
            TournamentParticipantRepository participantRepository,
            CompetitiveRatingEngine ratingEngine,
            KnockoutSeedingPolicy knockoutSeedingPolicy,
            LeagueSeedingPolicy leagueSeedingPolicy
    ) {
        this.tournamentRepository = tournamentRepository;
        this.participantRepository = participantRepository;
        this.ratingEngine = ratingEngine;
        this.knockoutSeedingPolicy = knockoutSeedingPolicy;
        this.leagueSeedingPolicy = leagueSeedingPolicy;
    }

    /**
     * Executes the full seeding pipeline for the given tournament.
     *
     * @param tournamentUuid the UUID of the tournament in SEEDING state
     */
    @Transactional
    public void executeSeedingPipeline(UUID tournamentUuid) {
        Tournament tournament = tournamentRepository.findByUuid(tournamentUuid)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found: " + tournamentUuid));

        log.info("[SEEDING] Starting seeding pipeline for tournament '{}' (type={})",
                tournament.getName(), tournament.getTournamentType());

        // Step 1: Load eligible participants
        List<TournamentParticipant> participants =
                participantRepository.findByTournamentIdOrderBySeedNumberAsc(tournament.getId());

        if (participants.isEmpty()) {
            log.warn("[SEEDING] No participants found for tournament {}. Seeding aborted.", tournamentUuid);
            return;
        }

        // Step 2: Compute competitive assessments (internal, never exposed)
        List<CompetitiveAssessment> assessments = ratingEngine.assess(tournament.getId(), participants);

        log.info("[SEEDING] Assessed {} participants: {} STRONG, {} MODERATE, {} WEAK",
                participants.size(),
                assessments.stream().filter(a -> a.tier() == StrengthTier.STRONG).count(),
                assessments.stream().filter(a -> a.tier() == StrengthTier.MODERATE).count(),
                assessments.stream().filter(a -> a.tier() == StrengthTier.WEAK).count());

        // Step 3: Select and apply seeding policy
        SeedingPolicy policy = selectPolicy(tournament);
        int groupCount = resolveGroupCount(tournament);
        List<TournamentParticipant> seeded = policy.applySeedingOrder(assessments, groupCount);

        // Step 4: Persist updated seed numbers and status
        for (TournamentParticipant participant : seeded) {
            participant.setStatus(ParticipantStatus.SEEDED);
        }
        participantRepository.saveAll(seeded);

        log.info("[SEEDING] Seeding pipeline complete for tournament '{}': {} participants seeded.",
                tournament.getName(), seeded.size());
    }

    /**
     * Selects the appropriate seeding policy for the tournament format.
     */
    private SeedingPolicy selectPolicy(Tournament tournament) {
        return switch (tournament.getTournamentType()) {
            case LEAGUE -> leagueSeedingPolicy;
            case HYBRID -> leagueSeedingPolicy; // HYBRID starts with league group stage
            case KNOCKOUT, SWISS, BATTLE_ROYALE -> knockoutSeedingPolicy;
        };
    }

    /**
     * Determines how many groups/brackets to distribute participants into.
     *
     * <p>For LEAGUE/HYBRID: uses tournament's configured group count from stage config.
     * For KNOCKOUT: always 1 (single bracket).
     */
    private int resolveGroupCount(Tournament tournament) {
        if (tournament.getTournamentType() == TournamentType.LEAGUE
                || tournament.getTournamentType() == TournamentType.HYBRID) {
            // Derive from stage configuration (first stage = group stage)
            return tournament.getStages().stream()
                    .filter(s -> s.getStageOrder() == 1)
                    .findFirst()
                    .map(stage -> Math.max(1, stage.getParticipantCount() / 4)) // default: groups of 4
                    .orElse(1);
        }
        return 1;
    }
}
