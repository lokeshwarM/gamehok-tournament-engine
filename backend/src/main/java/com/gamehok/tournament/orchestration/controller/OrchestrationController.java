package com.gamehok.tournament.orchestration.controller;

import com.gamehok.tournament.common.dto.ApiResponse;
import com.gamehok.tournament.orchestration.service.TournamentOrchestrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Internal orchestration controller for tournament lifecycle management.
 * All endpoints require organizer/admin privileges.
 */
@RestController
@RequestMapping("/api/v1/orchestration/tournaments")
@Tag(name = "Orchestration", description = "Tournament lifecycle orchestration (seeding, bracket generation, stage advancement)")
@PreAuthorize("hasRole('TOURNAMENT_ORGANIZER') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
public class OrchestrationController {

    private final TournamentOrchestrationService orchestrationService;

    public OrchestrationController(TournamentOrchestrationService orchestrationService) {
        this.orchestrationService = orchestrationService;
    }

    @PostMapping("/{uuid}/seed-and-generate")
    @Operation(summary = "Seed participants and generate initial bracket")
    public ResponseEntity<ApiResponse<Void>> seedAndGenerate(@PathVariable UUID uuid) {
        orchestrationService.seedAndGenerateBracket(uuid);
        return ResponseEntity.ok(ApiResponse.success("Bracket generated", null));
    }

    @PostMapping("/{uuid}/advance-stage/{stageId}")
    @Operation(summary = "Advance tournament to the next stage")
    public ResponseEntity<ApiResponse<Void>> advanceStage(
            @PathVariable UUID uuid,
            @PathVariable Long stageId
    ) {
        orchestrationService.advanceToNextStage(uuid, stageId);
        return ResponseEntity.ok(ApiResponse.success("Advanced to next stage", null));
    }

    @PostMapping("/{uuid}/complete")
    @Operation(summary = "Finalize tournament: compute rankings and distribute prizes")
    public ResponseEntity<ApiResponse<Void>> completeTournament(@PathVariable UUID uuid) {
        orchestrationService.completeTournament(uuid);
        return ResponseEntity.ok(ApiResponse.success("Tournament completed", null));
    }
}
