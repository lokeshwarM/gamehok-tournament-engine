package com.gamehok.tournament.prize.dto;

import com.gamehok.tournament.enums.PrizeType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Request DTO for configuring a prize allocation for a rank placement.
 */
@Getter
@NoArgsConstructor
public class PrizeAllocationRequest {

    @NotNull
    @Min(1)
    private Integer rankPlacement;

    @NotNull
    private PrizeType prizeType;

    private BigDecimal cashAmount;
    private Integer creditAmount;
    private String itemDescription;
    private String trophyName;
    private String badgeCode;
}
