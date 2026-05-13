package com.gamehok.tournament.tournament.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Request DTO for updating an existing tournament (partial update).
 */
@Getter
@NoArgsConstructor
public class UpdateTournamentRequest {

    @Size(min = 3, max = 150)
    private String name;

    @Size(max = 5000)
    private String description;

    private Instant registrationEnd;
    private Instant checkInStart;
    private Instant checkInEnd;
    private Instant startTime;

    @Size(max = 50)
    private String platform;

    @Size(max = 50)
    private String region;

    private BigDecimal prizePool;
    private Boolean featured;
}
