package com.gamehok.tournament.team.controller;

import com.gamehok.tournament.common.dto.ApiResponse;
import com.gamehok.tournament.common.dto.PageResponse;
import com.gamehok.tournament.team.dto.CreateTeamRequest;
import com.gamehok.tournament.team.dto.TeamResponseDto;
import com.gamehok.tournament.team.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST controller for team management endpoints.
 */
@RestController
@RequestMapping("/api/v1/teams")
@Tag(name = "Teams", description = "Team creation, roster management, and search")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping
    @Operation(summary = "Create a new team")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<TeamResponseDto>> createTeam(@Valid @RequestBody CreateTeamRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Team created successfully", teamService.createTeam(request)));
    }

    @GetMapping("/{uuid}")
    @Operation(summary = "Get team by UUID")
    public ResponseEntity<ApiResponse<TeamResponseDto>> getTeam(@PathVariable UUID uuid) {
        return ResponseEntity.ok(ApiResponse.success(teamService.getTeamByUuid(uuid)));
    }

    @GetMapping("/search")
    @Operation(summary = "Search teams by name")
    public ResponseEntity<ApiResponse<PageResponse<TeamResponseDto>>> searchTeams(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(ApiResponse.success(teamService.searchTeams(query, page, size)));
    }

    @PostMapping("/{teamUuid}/members/{userUuid}")
    @Operation(summary = "Add a member to the team")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<TeamResponseDto>> addMember(
            @PathVariable UUID teamUuid,
            @PathVariable UUID userUuid
    ) {
        return ResponseEntity.ok(ApiResponse.success("Member added", teamService.addMember(teamUuid, userUuid)));
    }

    @DeleteMapping("/{teamUuid}/members/{userUuid}")
    @Operation(summary = "Remove a member from the team")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> removeMember(
            @PathVariable UUID teamUuid,
            @PathVariable UUID userUuid
    ) {
        teamService.removeMember(teamUuid, userUuid);
        return ResponseEntity.ok(ApiResponse.success("Member removed", null));
    }

    @DeleteMapping("/{uuid}/disband")
    @Operation(summary = "Disband a team")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> disbandTeam(@PathVariable UUID uuid) {
        teamService.disbandTeam(uuid);
        return ResponseEntity.ok(ApiResponse.success("Team disbanded", null));
    }
}
