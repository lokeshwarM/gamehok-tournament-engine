package com.gamehok.tournament.engine.seeding;

import com.gamehok.tournament.enums.SeedingStrategy;
import com.gamehok.tournament.tournament.entity.TournamentParticipant;

import java.util.List;

/**
 * Strategy interface for participant seeding before bracket generation.
 * <p>
 * Implementations determine the order in which participants are placed into bracket slots.
 * The seeded order is then used by {@link com.gamehok.tournament.engine.bracket.BracketGenerator}.
 * </p>
 */
public interface ParticipantSeedingStrategy {

    /**
     * Returns which seeding strategy this implementation supports.
     */
    SeedingStrategy getSupportedStrategy();

    /**
     * Seeds participants and assigns seed numbers.
     * Returns participants in seeded order (index 0 = seed 1).
     */
    List<TournamentParticipant> seed(List<TournamentParticipant> participants);
}
