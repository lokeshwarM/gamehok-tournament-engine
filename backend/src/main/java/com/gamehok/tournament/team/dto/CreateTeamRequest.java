package com.gamehok.tournament.team.dto;

import com.gamehok.tournament.enums.TeamType;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Request DTO for creating a new team.
 */
@Getter
@NoArgsConstructor
public class CreateTeamRequest {

    @NotBlank(message = "Team name is required")
    @Size(min = 2, max = 80, message = "Team name must be between 2 and 80 characters")
    private String name;

    @NotBlank(message = "Team tag is required")
    @Size(min = 2, max = 10, message = "Team tag must be between 2 and 10 characters")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "Team tag must be uppercase alphanumeric")
    private String tag;

    @Size(max = 500)
    private String description;

    @NotNull(message = "Team type is required")
    private TeamType teamType;

    @NotNull(message = "Max size is required")
    @Min(value = 1, message = "Team must have at least 1 member")
    @Max(value = 10, message = "Team cannot exceed 10 members")
    private Integer maxSize;

    @Size(min = 2, max = 3)
    private String countryCode;
}
