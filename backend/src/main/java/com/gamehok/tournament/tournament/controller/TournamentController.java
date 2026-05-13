package com.gamehok.tournament.tournament.controller;

import com.gamehok.tournament.common.dto.ApiResponse;
import com.gamehok.tournament.common.dto.PageResponse;
import com.gamehok.tournament.enums.TournamentStatus;
import com.gamehok.tournament.tournament.dto.*;
import com.gamehok.tournament.tournament.service.TournamentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for tournament management and participant operations.
 */
@RestController
@RequestMapping("/api/v1/tournaments")
@Tag(name = "Tournaments", description = "Tournament lifecycle, registration, and participant management")
public class TournamentController {

    private final TournamentService tournamentService;

    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @PostMapping
    @Operation(summary = "Create a new tournament")
    @PreAuthorize("hasRole('TOURNAMENT_ORGANIZER') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<TournamentDetailDto>> createTournament(
            @Valid @RequestBody CreateTournamentRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tournament created", tournamentService.createTournament(request)));
    }

    @GetMapping("/{uuid}")
    @Operation(summary = "Get tournament by UUID")
    public ResponseEntity<ApiResponse<TournamentDetailDto>> getTournament(@PathVariable UUID uuid) {
        return ResponseEntity.ok(ApiResponse.success(tournamentService.getTournamentByUuid(uuid)));
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "Get tournament by slug")
    public ResponseEntity<ApiResponse<TournamentDetailDto>> getTournamentBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(ApiResponse.success(tournamentService.getTournamentBySlug(slug)));
    }

    @GetMapping
    @Operation(summary = "Get public tournaments by status")
    public ResponseEntity<ApiResponse<PageResponse<TournamentSummaryDto>>> getPublicTournaments(
            @RequestParam(defaultValue = "REGISTRATION_OPEN") TournamentStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(ApiResponse.success(tournamentService.getPublicTournaments(status, page, size)));
    }

    @GetMapping("/featured")
    @Operation(summary = "Get featured tournaments")
    public ResponseEntity<ApiResponse<List<TournamentSummaryDto>>> getFeatured() {
        return ResponseEntity.ok(ApiResponse.success(tournamentService.getFeaturedTournaments()));
    }

    @GetMapping("/organizer/{uuid}")
    @Operation(summary = "Get tournaments by organizer UUID")
    public ResponseEntity<ApiResponse<PageResponse<TournamentSummaryDto>>> getByOrganizer(
            @PathVariable UUID uuid,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(ApiResponse.success(tournamentService.getTournamentsByOrganizer(uuid, page, size)));
    }

    @PutMapping("/{uuid}")
    @Operation(summary = "Update tournament details")
    @PreAuthorize("hasRole('TOURNAMENT_ORGANIZER') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<TournamentDetailDto>> updateTournament(
            @PathVariable UUID uuid,
            @Valid @RequestBody UpdateTournamentRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success("Tournament updated", tournamentService.updateTournament(uuid, request)));
    }

    @PostMapping("/{tournamentUuid}/register/user/{userUuid}")
    @Operation(summary = "Register a solo player for a tournament")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<TournamentDetailDto>> registerUser(
            @PathVariable UUID tournamentUuid,
            @PathVariable UUID userUuid
    ) {
        return ResponseEntity.ok(ApiResponse.success("Registered successfully",
                tournamentService.registerParticipant(tournamentUuid, userUuid, false)));
    }

    @PostMapping("/{tournamentUuid}/register/team/{teamUuid}")
    @Operation(summary = "Register a team for a tournament")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<TournamentDetailDto>> registerTeam(
            @PathVariable UUID tournamentUuid,
            @PathVariable UUID teamUuid
    ) {
        return ResponseEntity.ok(ApiResponse.success("Team registered successfully",
                tournamentService.registerParticipant(tournamentUuid, teamUuid, true)));
    }

    @PostMapping("/{tournamentUuid}/checkin/{participantUuid}")
    @Operation(summary = "Check-in a participant")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> checkIn(
            @PathVariable UUID tournamentUuid,
            @PathVariable UUID participantUuid
    ) {
        tournamentService.checkInParticipant(tournamentUuid, participantUuid);
        return ResponseEntity.ok(ApiResponse.success("Checked in successfully", null));
    }

    @DeleteMapping("/{tournamentUuid}/withdraw/{participantUuid}")
    @Operation(summary = "Withdraw from a tournament")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> withdraw(
            @PathVariable UUID tournamentUuid,
            @PathVariable UUID participantUuid
    ) {
        tournamentService.withdrawParticipant(tournamentUuid, participantUuid);
        return ResponseEntity.ok(ApiResponse.success("Withdrawn from tournament", null));
    }

    @PostMapping("/{uuid}/cancel")
    @Operation(summary = "Cancel a tournament")
    @PreAuthorize("hasRole('TOURNAMENT_ORGANIZER') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> cancelTournament(
            @PathVariable UUID uuid,
            @RequestParam String reason
    ) {
        tournamentService.cancelTournament(uuid, reason);
        return ResponseEntity.ok(ApiResponse.success("Tournament cancelled", null));
    }
}
