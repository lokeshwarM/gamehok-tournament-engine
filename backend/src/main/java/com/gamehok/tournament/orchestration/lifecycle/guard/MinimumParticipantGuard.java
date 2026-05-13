package com.gamehok.tournament.orchestration.lifecycle.guard;

import com.gamehok.tournament.common.exception.InvalidStateTransitionException;
import com.gamehok.tournament.orchestration.lifecycle.TransitionContext;
import com.gamehok.tournament.orchestration.lifecycle.TransitionGuard;
import com.gamehok.tournament.tournament.repository.TournamentParticipantRepository;
import org.springframework.stereotype.Component;

/**
 * Ensures the tournament has enough registered participants before progressing
 * past REGISTRATION_CLOSED.
 *
 * <p>Business rule: participant count must be >= tournament's minParticipants.
 * If not met, the tournament should be CANCELLED instead of proceeding.
 */
@Component
public class MinimumParticipantGuard implements TransitionGuard {

    private final TournamentParticipantRepository participantRepository;

    public MinimumParticipantGuard(TournamentParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    @Override
    public void validate(TransitionContext context) {
        long count = participantRepository.countByTournamentId(context.tournament().getId());
        int required = context.tournament().getMinParticipants();

        if (count < required) {
            throw new InvalidStateTransitionException(String.format(
                    "Tournament '%s' requires at least %d participants but only has %d registered. " +
                    "Tournament should be cancelled rather than started.",
                    context.tournament().getUuid(), required, count));
        }
    }
}
