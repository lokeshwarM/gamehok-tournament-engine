export interface LeaderboardEntry {
  rank: number;
  participantId: string;
  name: string;
  points: number;
  wins: number;
  losses: number;
  draws: number;
  matchesPlayed: number;
  qualified?: boolean;
}

interface LeaderboardTableProps {
  entries: LeaderboardEntry[];
}

export function LeaderboardTable({ entries }: LeaderboardTableProps) {
  return (
    <div className="w-full overflow-hidden rounded-xl border border-border bg-surface-100">
      <div className="overflow-x-auto">
        <table className="w-full text-left text-sm">
          <thead className="bg-surface-200/50 text-xs uppercase text-foreground/60 border-b border-border">
            <tr>
              <th className="px-6 py-4 font-semibold">Rank</th>
              <th className="px-6 py-4 font-semibold">Participant</th>
              <th className="px-6 py-4 font-semibold text-center">MP</th>
              <th className="px-6 py-4 font-semibold text-center">W</th>
              <th className="px-6 py-4 font-semibold text-center">D</th>
              <th className="px-6 py-4 font-semibold text-center">L</th>
              <th className="px-6 py-4 font-semibold text-right text-primary">Pts</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-border/50">
            {entries.map((entry) => (
              <tr 
                key={entry.participantId}
                className={`transition-colors hover:bg-surface-200/30 ${
                  entry.qualified ? 'border-l-4 border-l-success' : 'border-l-4 border-l-transparent'
                }`}
              >
                <td className="px-6 py-4 font-medium text-white">
                  <div className="flex items-center gap-2">
                    <span className="w-6 text-center">{entry.rank}</span>
                    {entry.rank <= 3 && (
                      <span className={`size-2 rounded-full ${
                        entry.rank === 1 ? 'bg-yellow-400' : 
                        entry.rank === 2 ? 'bg-slate-300' : 'bg-amber-600'
                      }`} />
                    )}
                  </div>
                </td>
                <td className="px-6 py-4 font-semibold text-white">{entry.name}</td>
                <td className="px-6 py-4 text-center">{entry.matchesPlayed}</td>
                <td className="px-6 py-4 text-center text-success">{entry.wins}</td>
                <td className="px-6 py-4 text-center text-foreground/70">{entry.draws}</td>
                <td className="px-6 py-4 text-center text-danger">{entry.losses}</td>
                <td className="px-6 py-4 text-right font-bold text-white text-base">{entry.points}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
