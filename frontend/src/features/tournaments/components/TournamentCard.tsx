import Link from 'next/link';
import { Tournament } from '@/types/tournament';

interface TournamentCardProps {
  tournament: Tournament;
}

export function TournamentCard({ tournament }: TournamentCardProps) {
  const statusColors = {
    DRAFT: 'bg-surface-300 text-foreground',
    REGISTRATION_OPEN: 'bg-success/20 text-success',
    IN_PROGRESS: 'bg-warning/20 text-warning',
    COMPLETED: 'bg-primary/20 text-primary',
    CANCELLED: 'bg-danger/20 text-danger',
  };

  return (
    <Link 
      href={`/tournaments/${tournament.uuid}`}
      className="group relative flex flex-col overflow-hidden rounded-xl border border-border bg-surface-100 transition-all hover:border-primary/50 hover:shadow-lg hover:shadow-primary/5"
    >
      {/* Banner Placeholder */}
      <div className="h-32 w-full bg-surface-200 relative overflow-hidden">
        {tournament.bannerUrl ? (
          // eslint-disable-next-line @next/next/no-img-element
          <img 
            src={tournament.bannerUrl} 
            alt={tournament.name} 
            className="h-full w-full object-cover transition-transform duration-500 group-hover:scale-105"
          />
        ) : (
          <div className="absolute inset-0 flex items-center justify-center bg-gradient-to-br from-surface-200 to-surface-300">
            <span className="text-4xl font-bold text-surface-50/50">
              {tournament.gameTitle.substring(0, 2).toUpperCase()}
            </span>
          </div>
        )}
        <div className="absolute top-3 right-3">
          <span className={`inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-semibold ${statusColors[tournament.status]}`}>
            {tournament.status.replace('_', ' ')}
          </span>
        </div>
      </div>

      <div className="flex flex-1 flex-col p-5">
        <div className="mb-1 flex items-center gap-2 text-xs font-medium text-foreground/60">
          <span>{tournament.gameTitle}</span>
          <span>•</span>
          <span>{tournament.tournamentType}</span>
        </div>
        
        <h3 className="mb-4 text-lg font-bold tracking-tight text-white line-clamp-1 group-hover:text-primary transition-colors">
          {tournament.name}
        </h3>

        <div className="mt-auto grid grid-cols-2 gap-4 text-sm">
          <div className="flex flex-col gap-1">
            <span className="text-foreground/50 text-xs">Prize Pool</span>
            <span className="font-semibold text-white">
              {tournament.prizePool ? `$${tournament.prizePool.toLocaleString()}` : 'TBD'}
            </span>
          </div>
          <div className="flex flex-col gap-1">
            <span className="text-foreground/50 text-xs">Participants</span>
            <span className="font-semibold text-white">
              {tournament.currentParticipants} / {tournament.maxParticipants}
            </span>
          </div>
        </div>
      </div>
    </Link>
  );
}
