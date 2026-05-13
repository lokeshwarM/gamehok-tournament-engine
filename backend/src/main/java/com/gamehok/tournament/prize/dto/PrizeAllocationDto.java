package com.gamehok.tournament.prize.dto;

import com.gamehok.tournament.enums.PrizeType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Prize allocation response DTO.
 */
@Getter
@Builder
public class PrizeAllocationDto {
    private final UUID uuid;
    private final Integer rankPlacement;
    private final PrizeType prizeType;
    private final BigDecimal cashAmount;
    private final Integer creditAmount;
    private final String itemDescription;
    private final String trophyName;
    private final String badgeCode;
    private final boolean distributed;
}
