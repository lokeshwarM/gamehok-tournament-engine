import { LeaderboardTable } from '@/features/leaderboard/components/LeaderboardTable';
import { MatchCard } from '@/features/matches/components/MatchCard';
import { Match } from '@/types/match';

const mockMatches: Match[] = [
  {
    uuid: 'm1',
    tournamentId: '1',
    stageId: 's1',
    roundNumber: 1,
    status: 'COMPLETED',
    bestOf: 3,
    participant1: { uuid: 'p1', name: 'Team Alpha', score: 2 },
    participant2: { uuid: 'p2', name: 'Team Beta', score: 1 },
    winnerId: 'p1',
  },
  {
    uuid: 'm2',
    tournamentId: '1',
    stageId: 's1',
    roundNumber: 2,
    status: 'IN_PROGRESS',
    bestOf: 3,
    participant1: { uuid: 'p1', name: 'Team Alpha' },
    participant2: { uuid: 'p3', name: 'Team Gamma' },
  }
];

export default function TournamentDetailPage({ params }: { params: { id: string } }) {
  return (
    <div className="mx-auto max-w-7xl px-4 py-8">
      {/* Header */}
      <div className="mb-8 rounded-xl border border-border bg-surface-100 p-8">
        <div className="flex items-start justify-between">
          <div>
            <div className="mb-2 flex items-center gap-3">
              <span className="inline-flex items-center rounded-full bg-warning/20 px-3 py-1 text-xs font-semibold text-warning">
                IN PROGRESS
              </span>
              <span className="text-sm font-medium text-foreground/60">Valorant • Knockout</span>
            </div>
            <h1 className="text-4xl font-bold text-white tracking-tight">Gamehok Summer Championship 2026</h1>
            <p className="mt-4 max-w-2xl text-foreground/80">
              The biggest summer event for Valorant. Compete for a massive $10,000 prize pool in this 32-team knockout bracket.
            </p>
          </div>
          <div className="flex flex-col items-end gap-2">
            <div className="text-right">
              <span className="block text-sm text-foreground/60">Prize Pool</span>
              <span className="text-2xl font-bold text-primary">$10,000</span>
            </div>
            <button className="mt-4 rounded-md bg-primary px-6 py-2 font-semibold text-white hover:bg-primary-hover transition-colors">
              Manage Tournament
            </button>
          </div>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        {/* Main Content Area */}
        <div className="lg:col-span-2 flex flex-col gap-8">
          <section>
            <h2 className="mb-4 text-xl font-bold text-white">Active Matches</h2>
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              {mockMatches.map(match => (
                <MatchCard key={match.uuid} match={match} />
              ))}
            </div>
          </section>

          <section>
            <h2 className="mb-4 text-xl font-bold text-white">Stage Standings</h2>
            <LeaderboardTable 
              entries={[
                { rank: 1, participantId: 'p1', name: 'Team Alpha', points: 9, wins: 3, draws: 0, losses: 0, matchesPlayed: 3, qualified: true },
                { rank: 2, participantId: 'p3', name: 'Team Gamma', points: 6, wins: 2, draws: 0, losses: 1, matchesPlayed: 3, qualified: true },
                { rank: 3, participantId: 'p2', name: 'Team Beta', points: 3, wins: 1, draws: 0, losses: 2, matchesPlayed: 3 },
                { rank: 4, participantId: 'p4', name: 'Team Delta', points: 0, wins: 0, draws: 0, losses: 3, matchesPlayed: 3 },
              ]}
            />
          </section>
        </div>

        {/* Sidebar info */}
        <div className="flex flex-col gap-6">
          <div className="rounded-xl border border-border bg-surface-100 p-6">
            <h3 className="mb-4 text-lg font-bold text-white">Information</h3>
            <dl className="flex flex-col gap-4 text-sm">
              <div className="flex justify-between">
                <dt className="text-foreground/60">Registration Ends</dt>
                <dd className="font-medium text-white">May 30, 2026</dd>
              </div>
              <div className="flex justify-between">
                <dt className="text-foreground/60">Starts</dt>
                <dd className="font-medium text-white">June 1, 2026</dd>
              </div>
              <div className="flex justify-between">
                <dt className="text-foreground/60">Team Size</dt>
                <dd className="font-medium text-white">5v5</dd>
              </div>
              <div className="flex justify-between">
                <dt className="text-foreground/60">Participants</dt>
                <dd className="font-medium text-white">32 / 32 Teams</dd>
              </div>
            </dl>
          </div>
        </div>
      </div>
    </div>
  );
}
