package com.gamehok.tournament.tournament.dto;

import com.gamehok.tournament.enums.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Request DTO for creating a new tournament.
 */
@Getter
@NoArgsConstructor
public class CreateTournamentRequest {

    @NotBlank(message = "Tournament name is required")
    @Size(min = 3, max = 150, message = "Tournament name must be between 3 and 150 characters")
    private String name;

    @Size(max = 5000)
    private String description;

    @NotNull(message = "Tournament type is required")
    private TournamentType tournamentType;

    @NotNull(message = "Team type is required")
    private TeamType teamType;

    @NotNull(message = "Team size is required")
    @Min(value = 1, message = "Team size must be at least 1")
    @Max(value = 10, message = "Team size cannot exceed 10")
    private Integer teamSize;

    @NotNull(message = "Min participants is required")
    @Min(value = 2)
    private Integer minParticipants;

    @NotNull(message = "Max participants is required")
    @Min(value = 2)
    @Max(value = 1024)
    private Integer maxParticipants;

    @NotNull(message = "Registration start time is required")
    private Instant registrationStart;

    @NotNull(message = "Registration end time is required")
    private Instant registrationEnd;

    private Instant checkInStart;
    private Instant checkInEnd;

    @NotNull(message = "Tournament start time is required")
    private Instant startTime;

    @NotBlank(message = "Game title is required")
    @Size(max = 100)
    private String gameTitle;

    @Size(max = 100)
    private String gameMode;

    @Size(max = 50)
    private String platform;

    @Size(max = 50)
    private String region;

    private SeedingStrategy seedingStrategy = SeedingStrategy.RANDOM;

    private boolean publicTournament = true;

    @DecimalMin(value = "0.0")
    private BigDecimal entryFee;

    @DecimalMin(value = "0.0")
    private BigDecimal prizePool;

    @Valid
    private List<StageConfigRequest> stages;
}
