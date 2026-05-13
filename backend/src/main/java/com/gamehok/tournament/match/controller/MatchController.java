package com.gamehok.tournament.match.controller;

import com.gamehok.tournament.common.dto.ApiResponse;
import com.gamehok.tournament.match.dto.MatchResponseDto;
import com.gamehok.tournament.match.dto.SubmitResultRequest;
import com.gamehok.tournament.match.service.MatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for match operations.
 */
@RestController
@RequestMapping("/api/v1/matches")
@Tag(name = "Matches", description = "Match retrieval, result submission, and dispute management")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping("/{uuid}")
    @Operation(summary = "Get match by UUID")
    public ResponseEntity<ApiResponse<MatchResponseDto>> getMatch(@PathVariable UUID uuid) {
        return ResponseEntity.ok(ApiResponse.success(matchService.getMatchByUuid(uuid)));
    }

    @GetMapping("/tournament/{tournamentUuid}/stage/{stageUuid}")
    @Operation(summary = "Get all matches for a tournament stage")
    public ResponseEntity<ApiResponse<List<MatchResponseDto>>> getMatchesByStage(
            @PathVariable UUID tournamentUuid,
            @PathVariable UUID stageUuid
    ) {
        return ResponseEntity.ok(ApiResponse.success(matchService.getMatchesByTournamentAndStage(tournamentUuid, stageUuid)));
    }

    @GetMapping("/participant/{uuid}")
    @Operation(summary = "Get all matches for a participant")
    public ResponseEntity<ApiResponse<List<MatchResponseDto>>> getMatchesByParticipant(@PathVariable UUID uuid) {
        return ResponseEntity.ok(ApiResponse.success(matchService.getMatchesByParticipant(uuid)));
    }

    @PostMapping("/{uuid}/result")
    @Operation(summary = "Submit match result")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<MatchResponseDto>> submitResult(
            @PathVariable UUID uuid,
            @Valid @RequestBody SubmitResultRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success("Result submitted", matchService.submitResult(uuid, request)));
    }

    @PostMapping("/{uuid}/dispute")
    @Operation(summary = "Dispute a match result")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<MatchResponseDto>> disputeResult(
            @PathVariable UUID uuid,
            @RequestParam String reason
    ) {
        return ResponseEntity.ok(ApiResponse.success("Dispute filed", matchService.disputeResult(uuid, reason)));
    }

    @PostMapping("/{uuid}/resolve-dispute")
    @Operation(summary = "Admin resolves a disputed match")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<MatchResponseDto>> resolveDispute(
            @PathVariable UUID uuid,
            @RequestParam UUID winnerId,
            @RequestParam String adminNote
    ) {
        return ResponseEntity.ok(ApiResponse.success("Dispute resolved", matchService.resolveDispute(uuid, winnerId, adminNote)));
    }

    @PostMapping("/{uuid}/admin-override")
    @Operation(summary = "Admin override of match result")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<MatchResponseDto>> adminOverride(
            @PathVariable UUID uuid,
            @RequestParam UUID winnerId,
            @RequestParam String reason
    ) {
        return ResponseEntity.ok(ApiResponse.success("Result overridden", matchService.adminOverrideResult(uuid, winnerId, reason)));
    }
}
