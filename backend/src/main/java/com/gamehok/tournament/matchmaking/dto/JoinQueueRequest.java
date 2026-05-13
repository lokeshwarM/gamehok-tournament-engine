package com.gamehok.tournament.matchmaking.dto;

import com.gamehok.tournament.enums.TeamType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Request DTO to join the matchmaking queue.
 */
@Getter
@NoArgsConstructor
public class JoinQueueRequest {

    @NotNull
    private TeamType teamType;

    @NotBlank
    @Size(max = 100)
    private String gameTitle;

    @Size(max = 100)
    private String gameMode;

    @Size(max = 50)
    private String region;

    private java.util.UUID teamUuid;
}
