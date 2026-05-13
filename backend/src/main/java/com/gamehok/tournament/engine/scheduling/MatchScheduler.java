package com.gamehok.tournament.engine.scheduling;

import com.gamehok.tournament.match.entity.Match;
import com.gamehok.tournament.tournament.entity.TournamentStage;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Match scheduling engine responsible for assigning start times to matches.
 * <p>
 * Supports:
 * - Fixed schedule: matches scheduled at specific pre-defined times
 * - Dynamic schedule: matches scheduled based on availability and previous round completion
 * - Concurrent scheduling: multiple matches in the same time slot (different rooms/lobbies)
 * </p>
 */
public interface MatchScheduler {

    /**
     * Schedules all matches in a stage based on stage start time and match duration.
     *
     * @param stage          the tournament stage
     * @param matches        the matches to schedule
     * @param stageStartTime the time from which to start scheduling
     * @param matchDurationMinutes estimated duration per match in minutes
     * @return map of matchId → scheduled start time
     */
    Map<Long, Instant> scheduleMatches(
            TournamentStage stage,
            List<Match> matches,
            Instant stageStartTime,
            int matchDurationMinutes
    );
}
