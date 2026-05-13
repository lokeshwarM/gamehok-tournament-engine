export type MatchStatus = 'SCHEDULED' | 'IN_PROGRESS' | 'COMPLETED' | 'DISPUTED' | 'CANCELLED';

export interface MatchParticipant {
  uuid: string;
  name: string;
  avatarUrl?: string;
  score?: number;
}

export interface Match {
  uuid: string;
  tournamentId: string;
  stageId: string;
  roundNumber: number;
  status: MatchStatus;
  participant1?: MatchParticipant;
  participant2?: MatchParticipant;
  winnerId?: string;
  scheduledAt?: string;
  bestOf: number;
}
