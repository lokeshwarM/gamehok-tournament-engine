package com.gamehok.tournament.prize.dto;

import com.gamehok.tournament.enums.PrizeType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Prize distribution result DTO.
 */
@Getter
@Builder
public class PrizeDistributionDto {
    private final UUID uuid;
    private final UUID participantUuid;
    private final Integer rankAchieved;
    private final PrizeType prizeType;
    private final BigDecimal cashAmount;
    private final String description;
    private final String status;
    private final Instant distributedAt;
}
