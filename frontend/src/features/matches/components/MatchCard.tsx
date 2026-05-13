import { Match } from '@/types/match';

interface MatchCardProps {
  match: Match;
}

export function MatchCard({ match }: MatchCardProps) {
  const isCompleted = match.status === 'COMPLETED';
  
  return (
    <div className="flex flex-col overflow-hidden rounded-lg border border-border bg-surface-100 hover:border-surface-300 transition-colors">
      <div className="flex items-center justify-between border-b border-border bg-surface-200/50 px-4 py-2 text-xs font-medium text-foreground/70">
        <span>Round {match.roundNumber}</span>
        <span className="flex items-center gap-2">
          {match.status === 'IN_PROGRESS' && (
            <span className="relative flex size-2">
              <span className="absolute inline-flex h-full w-full animate-ping rounded-full bg-danger opacity-75"></span>
              <span className="relative inline-flex size-2 rounded-full bg-danger"></span>
            </span>
          )}
          {match.status.replace('_', ' ')}
        </span>
      </div>
      
      <div className="flex flex-col p-4 gap-3">
        {/* Participant 1 */}
        <div className={`flex items-center justify-between rounded p-2 transition-colors ${isCompleted && match.winnerId === match.participant1?.uuid ? 'bg-primary/10' : ''}`}>
          <div className="flex items-center gap-3">
            <div className="size-8 rounded-full bg-surface-300 flex items-center justify-center text-xs font-bold overflow-hidden">
              {match.participant1?.avatarUrl ? (
                // eslint-disable-next-line @next/next/no-img-element
                <img src={match.participant1.avatarUrl} alt="" className="size-full object-cover" />
              ) : (
                match.participant1?.name.substring(0, 2).toUpperCase() || 'TBD'
              )}
            </div>
            <span className={`font-semibold ${isCompleted && match.winnerId === match.participant1?.uuid ? 'text-white' : 'text-foreground/90'}`}>
              {match.participant1?.name || 'TBD'}
            </span>
          </div>
          <span className={`text-lg font-bold ${isCompleted && match.winnerId === match.participant1?.uuid ? 'text-white' : 'text-foreground/50'}`}>
            {match.participant1?.score ?? '-'}
          </span>
        </div>

        {/* VS Divider */}
        <div className="relative flex items-center justify-center">
          <div className="absolute inset-0 flex items-center">
            <div className="w-full border-t border-border/50"></div>
          </div>
          <div className="relative bg-surface-100 px-2 text-[10px] font-bold tracking-widest text-foreground/40 uppercase">
            BO{match.bestOf}
          </div>
        </div>

        {/* Participant 2 */}
        <div className={`flex items-center justify-between rounded p-2 transition-colors ${isCompleted && match.winnerId === match.participant2?.uuid ? 'bg-primary/10' : ''}`}>
          <div className="flex items-center gap-3">
            <div className="size-8 rounded-full bg-surface-300 flex items-center justify-center text-xs font-bold overflow-hidden">
              {match.participant2?.avatarUrl ? (
                // eslint-disable-next-line @next/next/no-img-element
                <img src={match.participant2.avatarUrl} alt="" className="size-full object-cover" />
              ) : (
                match.participant2?.name.substring(0, 2).toUpperCase() || 'TBD'
              )}
            </div>
            <span className={`font-semibold ${isCompleted && match.winnerId === match.participant2?.uuid ? 'text-white' : 'text-foreground/90'}`}>
              {match.participant2?.name || 'TBD'}
            </span>
          </div>
          <span className={`text-lg font-bold ${isCompleted && match.winnerId === match.participant2?.uuid ? 'text-white' : 'text-foreground/50'}`}>
            {match.participant2?.score ?? '-'}
          </span>
        </div>
      </div>
    </div>
  );
}
