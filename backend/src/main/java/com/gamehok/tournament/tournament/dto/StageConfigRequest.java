package com.gamehok.tournament.tournament.dto;

import com.gamehok.tournament.enums.StageType;
import com.gamehok.tournament.enums.TournamentType;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Stage configuration embedded in {@link CreateTournamentRequest}.
 */
@Getter
@NoArgsConstructor
public class StageConfigRequest {

    @NotBlank
    @Size(max = 100)
    private String stageName;

    @NotNull
    private StageType stageType;

    @NotNull
    private TournamentType format;

    @NotNull
    @Min(1)
    private Integer stageOrder;

    @NotNull
    @Min(2)
    private Integer participantCount;

    @Min(1)
    private Integer qualifiersCount;

    @NotNull
    @Min(1)
    @Max(7)
    private Integer bestOf = 1;

    private boolean doubleElimination = false;
    private boolean thirdPlaceMatch = false;
}
