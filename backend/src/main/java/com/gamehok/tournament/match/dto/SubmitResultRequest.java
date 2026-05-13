package com.gamehok.tournament.match.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Request DTO for submitting a match result.
 */
@Getter
@NoArgsConstructor
public class SubmitResultRequest {

    @NotNull(message = "Reported winner is required")
    private UUID reportedWinnerUuid;

    @Min(0)
    private Integer participant1Score;

    @Min(0)
    private Integer participant2Score;

    @Size(max = 500)
    private String screenshotUrl;

    private String scoreJson;
}
