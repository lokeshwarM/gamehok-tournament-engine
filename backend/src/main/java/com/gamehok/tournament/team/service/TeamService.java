package com.gamehok.tournament.team.service;

import com.gamehok.tournament.common.dto.PageResponse;
import com.gamehok.tournament.team.dto.CreateTeamRequest;
import com.gamehok.tournament.team.dto.TeamResponseDto;

import java.util.UUID;

/**
 * Team domain service contract.
 */
public interface TeamService {

    TeamResponseDto createTeam(CreateTeamRequest request);

    TeamResponseDto getTeamByUuid(UUID uuid);

    PageResponse<TeamResponseDto> searchTeams(String query, int page, int size);

    TeamResponseDto addMember(UUID teamUuid, UUID userUuid);

    void removeMember(UUID teamUuid, UUID userUuid);

    void disbandTeam(UUID teamUuid);
}
