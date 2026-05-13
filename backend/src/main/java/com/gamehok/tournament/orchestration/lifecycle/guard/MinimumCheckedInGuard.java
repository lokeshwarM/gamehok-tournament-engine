package com.gamehok.tournament.orchestration.lifecycle.guard;

import com.gamehok.tournament.common.exception.InvalidStateTransitionException;
import com.gamehok.tournament.enums.ParticipantStatus;
import com.gamehok.tournament.orchestration.lifecycle.TransitionContext;
import com.gamehok.tournament.orchestration.lifecycle.TransitionGuard;
import com.gamehok.tournament.tournament.repository.TournamentParticipantRepository;
import org.springframework.stereotype.Component;

/**
 * Ensures enough participants checked in before seeding begins.
 *
 * <p>Business rule: at least {@code minParticipants} participants must be in CHECKED_IN status.
 * Participants who registered but did not check in are treated as no-shows.
 */
@Component
public class MinimumCheckedInGuard implements TransitionGuard {

    private final TournamentParticipantRepository participantRepository;

    public MinimumCheckedInGuard(TournamentParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    @Override
    public void validate(TransitionContext context) {
        long checkedIn = participantRepository.countByTournamentIdAndStatus(
                context.tournament().getId(),
                ParticipantStatus.CHECKED_IN
        );

        int required = context.tournament().getMinParticipants();

        if (checkedIn < required) {
            throw new InvalidStateTransitionException(String.format(
                    "Tournament '%s' requires at least %d checked-in participants, but only %d checked in. " +
                    "Cannot proceed to seeding.",
                    context.tournament().getUuid(), required, checkedIn));
        }
    }
}
