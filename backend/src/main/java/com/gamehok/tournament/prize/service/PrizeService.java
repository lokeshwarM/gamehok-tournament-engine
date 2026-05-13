package com.gamehok.tournament.prize.service;

import java.util.UUID;

/**
 * Prize management service contract.
 */
public interface PrizeService {

    void configurePrizePool(UUID tournamentUuid, java.util.List<com.gamehok.tournament.prize.dto.PrizeAllocationRequest> allocations);

    java.util.List<com.gamehok.tournament.prize.dto.PrizeAllocationDto> getPrizePool(UUID tournamentUuid);

    void distributePrizes(UUID tournamentUuid);

    java.util.List<com.gamehok.tournament.prize.dto.PrizeDistributionDto> getDistributions(UUID tournamentUuid);
}
