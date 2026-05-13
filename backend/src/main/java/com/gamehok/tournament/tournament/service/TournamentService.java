package com.gamehok.tournament.tournament.service;

import com.gamehok.tournament.common.dto.PageResponse;
import com.gamehok.tournament.tournament.dto.*;
import com.gamehok.tournament.enums.TournamentStatus;

import java.util.List;
import java.util.UUID;

public interface TournamentService {

    TournamentDetailDto createTournament(CreateTournamentRequest request);

    TournamentDetailDto getTournamentByUuid(UUID uuid);

    TournamentDetailDto getTournamentBySlug(String slug);

    PageResponse<TournamentSummaryDto> getPublicTournaments(TournamentStatus status, int page, int size);

    PageResponse<TournamentSummaryDto> getTournamentsByOrganizer(UUID organizerUuid, int page, int size);

    List<TournamentSummaryDto> getFeaturedTournaments();

    TournamentDetailDto updateTournament(UUID uuid, UpdateTournamentRequest request);

    TournamentDetailDto registerParticipant(UUID tournamentUuid, UUID participantUuid, boolean isTeam);

    void checkInParticipant(UUID tournamentUuid, UUID participantUuid);

    void withdrawParticipant(UUID tournamentUuid, UUID participantUuid);

    void cancelTournament(UUID uuid, String reason);
}
