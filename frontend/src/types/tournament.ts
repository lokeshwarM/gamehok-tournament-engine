export type TournamentStatus = 'DRAFT' | 'REGISTRATION_OPEN' | 'IN_PROGRESS' | 'COMPLETED' | 'CANCELLED';
export type TournamentType = 'KNOCKOUT' | 'LEAGUE' | 'HYBRID' | 'SWISS';

export interface Tournament {
  uuid: string;
  name: string;
  slug: string;
  description?: string;
  gameTitle: string;
  bannerUrl?: string;
  status: TournamentStatus;
  tournamentType: TournamentType;
  teamSize: number;
  maxParticipants: number;
  currentParticipants: number;
  prizePool?: number;
  startTime: string;
  registrationEnd: string;
}
