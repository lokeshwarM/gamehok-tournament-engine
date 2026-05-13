package com.gamehok.tournament.enums;

/**
 * Seeding strategies used to assign initial bracket positions.
 */
public enum SeedingStrategy {

    RANDOM,           // Fully randomized placement
    ELO_BASED,        // Ranked by ELO/MMR, top seeds separated
    MANUAL,           // Organizer manually assigns seeds
    REGISTRATION_ORDER, // First-come-first-served
    PERFORMANCE_BASED  // Based on prior tournament performance
}
