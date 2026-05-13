package com.gamehok.tournament.matchmaking.controller;

import com.gamehok.tournament.common.dto.ApiResponse;
import com.gamehok.tournament.matchmaking.dto.JoinQueueRequest;
import com.gamehok.tournament.matchmaking.dto.QueueStatusDto;
import com.gamehok.tournament.matchmaking.service.MatchmakingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST controller for quick matchmaking queue operations.
 */
@RestController
@RequestMapping("/api/v1/matchmaking")
@Tag(name = "Matchmaking", description = "Quick matchmaking queue for casual and ranked games")
@PreAuthorize("isAuthenticated()")
public class MatchmakingController {

    private final MatchmakingService matchmakingService;

    public MatchmakingController(MatchmakingService matchmakingService) {
        this.matchmakingService = matchmakingService;
    }

    @PostMapping("/queue/join")
    @Operation(summary = "Join the matchmaking queue")
    public ResponseEntity<ApiResponse<QueueStatusDto>> joinQueue(@Valid @RequestBody JoinQueueRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Joined queue", matchmakingService.joinQueue(request)));
    }

    @GetMapping("/queue/{uuid}/status")
    @Operation(summary = "Check matchmaking queue status")
    public ResponseEntity<ApiResponse<QueueStatusDto>> getStatus(@PathVariable UUID uuid) {
        return ResponseEntity.ok(ApiResponse.success(matchmakingService.getQueueStatus(uuid)));
    }

    @DeleteMapping("/queue/{uuid}/leave")
    @Operation(summary = "Leave the matchmaking queue")
    public ResponseEntity<ApiResponse<Void>> leaveQueue(@PathVariable UUID uuid) {
        matchmakingService.leaveQueue(uuid);
        return ResponseEntity.ok(ApiResponse.success("Left queue", null));
    }
}
