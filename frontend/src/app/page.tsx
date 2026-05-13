import Link from 'next/link';
import { TournamentCard } from '@/features/tournaments/components/TournamentCard';
import { Tournament } from '@/types/tournament';

const featuredTournaments: Tournament[] = [
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
    uuid: '3',
    name: 'Pro League Season 4',
    slug: 'pro-league-s4',
    gameTitle: 'CS:GO 2',
    status: 'REGISTRATION_OPEN',
    tournamentType: 'LEAGUE',
    teamSize: 5,
    maxParticipants: 16,
    currentParticipants: 12,
    prizePool: 25000,
    startTime: '2026-07-01T10:00:00Z',
    registrationEnd: '2026-06-25T10:00:00Z',
  },
  {
    uuid: '4',
    name: 'Weekend Brawl #42',
    slug: 'weekend-brawl-42',
    gameTitle: 'Super Smash Bros',
    status: 'DRAFT',
    tournamentType: 'KNOCKOUT',
    teamSize: 1,
    maxParticipants: 64,
    currentParticipants: 0,
    prizePool: 1000,
    startTime: '2026-05-25T14:00:00Z',
    registrationEnd: '2026-05-24T23:59:00Z',
  }
];

export default function HomePage() {
  return (
    <div className="flex-1 flex flex-col">
      {/* Hero Section */}
      <section className="relative overflow-hidden bg-surface-100 py-24 sm:py-32 border-b border-border">
        {/* Background gradient effect */}
        <div className="absolute inset-x-0 top-[-10rem] -z-10 transform-gpu overflow-hidden blur-3xl sm:top-[-20rem]">
          <div className="relative left-1/2 -z-10 aspect-[1155/678] w-[36.125rem] max-w-none -translate-x-1/2 rotate-[30deg] bg-gradient-to-tr from-primary to-accent opacity-20 sm:left-[calc(50%-40rem)] sm:w-[72.1875rem]"></div>
        </div>
        
        <div className="mx-auto max-w-7xl px-6 lg:px-8 text-center">
          <h1 className="mx-auto max-w-4xl text-5xl font-bold tracking-tight text-white sm:text-7xl">
            The Ultimate Esports <br className="hidden sm:block" />
            <span className="text-transparent bg-clip-text bg-gradient-to-r from-primary to-accent">
              Tournament Engine
            </span>
          </h1>
          <p className="mx-auto mt-6 max-w-2xl text-lg leading-8 text-foreground/80">
            A production-grade platform for organizing, managing, and competing in professional esports tournaments. Built for scale, designed for champions.
          </p>
          <div className="mt-10 flex items-center justify-center gap-x-6">
            <Link
              href="/dashboard"
              className="rounded-md bg-primary px-5 py-3 text-sm font-semibold text-white shadow-sm hover:bg-primary-hover focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-primary transition-colors"
            >
              Go to Dashboard
            </Link>
            <Link href="/tournaments" className="text-sm font-semibold leading-6 text-white hover:text-primary transition-colors">
              Browse Tournaments <span aria-hidden="true">→</span>
            </Link>
          </div>
        </div>
      </section>

      {/* Featured Tournaments Section */}
      <section className="py-20">
        <div className="mx-auto max-w-7xl px-6 lg:px-8">
          <div className="mx-auto max-w-2xl md:text-center mb-12">
            <h2 className="text-base font-semibold leading-7 text-primary">Compete & Win</h2>
            <p className="mt-2 text-3xl font-bold tracking-tight text-white sm:text-4xl">
              Featured Tournaments
            </p>
          </div>
          
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
            {featuredTournaments.map(tournament => (
              <TournamentCard key={tournament.uuid} tournament={tournament} />
            ))}
          </div>
        </div>
      </section>
    </div>
  );
}
