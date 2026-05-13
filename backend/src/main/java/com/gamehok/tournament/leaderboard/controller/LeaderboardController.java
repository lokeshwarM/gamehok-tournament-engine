package com.gamehok.tournament.leaderboard.controller;

import com.gamehok.tournament.common.dto.ApiResponse;
import com.gamehok.tournament.leaderboard.dto.LeaderboardEntryDto;
import com.gamehok.tournament.leaderboard.service.LeaderboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for leaderboard and standings endpoints.
 */
@RestController
@RequestMapping("/api/v1/leaderboard")
@Tag(name = "Leaderboard", description = "Tournament and stage standings")
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    public LeaderboardController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    @GetMapping("/tournament/{tournamentUuid}")
    @Operation(summary = "Get overall tournament standings")
    public ResponseEntity<ApiResponse<List<LeaderboardEntryDto>>> getTournamentStandings(
            @PathVariable UUID tournamentUuid
    ) {
        return ResponseEntity.ok(ApiResponse.success(leaderboardService.getTournamentStandings(tournamentUuid)));
    }

    @GetMapping("/tournament/{tournamentUuid}/stage/{stageUuid}")
    @Operation(summary = "Get stage-level standings")
    public ResponseEntity<ApiResponse<List<LeaderboardEntryDto>>> getStageStandings(
            @PathVariable UUID tournamentUuid,
            @PathVariable UUID stageUuid
    ) {
        return ResponseEntity.ok(ApiResponse.success(leaderboardService.getStageStandings(tournamentUuid, stageUuid)));
    }
}
