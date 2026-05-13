import { TournamentCard } from '@/features/tournaments/components/TournamentCard';
import { Tournament } from '@/types/tournament';

// Mock data for architecture demonstration
const mockTournaments: Tournament[] = [
  {
    uuid: '1',
    name: 'Gamehok Summer Championship 2026',
    slug: 'gamehok-summer-2026',
    gameTitle: 'Valorant',
    status: 'IN_PROGRESS',
    tournamentType: 'KNOCKOUT',
    teamSize: 5,
    maxParticipants: 32,
    currentParticipants: 32,
    prizePool: 10000,
    startTime: '2026-06-01T10:00:00Z',
    registrationEnd: '2026-05-30T10:00:00Z',
  },
  {
    uuid: '2',
    name: 'Weekly Blitz Solo',
    slug: 'weekly-blitz-solo-14',
    gameTitle: 'Rocket League',
    status: 'REGISTRATION_OPEN',
    tournamentType: 'SWISS',
    teamSize: 1,
    maxParticipants: 128,
    currentParticipants: 84,
    prizePool: 500,
    startTime: '2026-05-20T15:00:00Z',
    registrationEnd: '2026-05-20T14:00:00Z',
  }
];

export default function DashboardPage() {
  return (
    <div className="flex flex-col gap-8">
      <div>
        <h1 className="text-3xl font-bold tracking-tight text-white">Dashboard</h1>
        <p className="mt-2 text-foreground/70">
          Welcome back. Here is an overview of your active tournaments and upcoming matches.
        </p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        {/* Stats widgets */}
        <div className="rounded-xl border border-border bg-surface-100 p-6">
          <h3 className="text-sm font-medium text-foreground/60">Active Tournaments</h3>
          <p className="mt-2 text-3xl font-bold text-white">2</p>
        </div>
        <div className="rounded-xl border border-border bg-surface-100 p-6">
          <h3 className="text-sm font-medium text-foreground/60">Upcoming Matches</h3>
          <p className="mt-2 text-3xl font-bold text-white">5</p>
        </div>
        <div className="rounded-xl border border-border bg-surface-100 p-6">
          <h3 className="text-sm font-medium text-foreground/60">Total Earnings</h3>
          <p className="mt-2 text-3xl font-bold text-primary">$1,250</p>
        </div>
      </div>

      <div>
        <div className="mb-4 flex items-center justify-between">
          <h2 className="text-xl font-bold text-white">Your Tournaments</h2>
          <button className="text-sm font-medium text-primary hover:text-primary-hover">View all</button>
        </div>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {mockTournaments.map(tournament => (
            <TournamentCard key={tournament.uuid} tournament={tournament} />
          ))}
        </div>
      </div>
    </div>
  );
}
