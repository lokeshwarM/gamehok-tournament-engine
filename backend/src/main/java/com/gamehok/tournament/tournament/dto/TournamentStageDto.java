package com.gamehok.tournament.tournament.dto;

import com.gamehok.tournament.enums.StageType;
import com.gamehok.tournament.enums.TournamentStatus;
import com.gamehok.tournament.enums.TournamentType;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

/**
 * Tournament stage DTO embedded within {@link TournamentDetailDto}.
 */
@Getter
@Builder
public class TournamentStageDto {

    private final UUID uuid;
    private final String stageName;
    private final StageType stageType;
    private final TournamentType format;
    private final TournamentStatus status;
    private final Integer stageOrder;
    private final Integer participantCount;
    private final Integer qualifiersCount;
    private final Integer bestOf;
    private final boolean doubleElimination;
    private final boolean thirdPlaceMatch;
    private final boolean completed;
}
