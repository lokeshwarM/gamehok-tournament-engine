package com.gamehok.tournament;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main entry point for the Gamehok Tournament Engine.
 * <p>
 * Modular monolith architecture supporting:
 * - Knockout, League, and Hybrid tournament formats
 * - Quick matchmaking systems
 * - Multi-stage progression with configurable qualification logic
 * - Flexible team sizes (1v1 to 5v5)
 * </p>
 */
@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAwareProvider")
@EnableAsync
@EnableScheduling
@EnableCaching
public class TournamentEngineApplication {

    public static void main(String[] args) {
        SpringApplication.run(TournamentEngineApplication.class, args);
    }
}
