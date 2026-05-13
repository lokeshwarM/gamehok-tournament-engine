import Link from 'next/link';

export function Navbar() {
  return (
    <header className="sticky top-0 z-50 w-full border-b border-border bg-surface-100/80 backdrop-blur">
      <div className="container mx-auto flex h-16 items-center justify-between px-4">
        <div className="flex items-center gap-6">
          <Link href="/" className="flex items-center gap-2">
            <div className="size-8 rounded bg-primary flex items-center justify-center font-bold text-white">
              G
            </div>
            <span className="text-xl font-bold tracking-tight text-white">Gamehok</span>
          </Link>
          <nav className="hidden md:flex items-center gap-6 text-sm font-medium text-foreground/80">
            <Link href="/tournaments" className="hover:text-white transition-colors">Tournaments</Link>
            <Link href="/leaderboard" className="hover:text-white transition-colors">Leaderboard</Link>
          </nav>
        </div>
        <div className="flex items-center gap-4">
          <Link 
            href="/dashboard" 
            className="text-sm font-medium hover:text-white transition-colors"
          >
            Dashboard
          </Link>
          <button className="rounded-md bg-primary px-4 py-2 text-sm font-medium text-white transition-colors hover:bg-primary-hover">
            Sign In
          </button>
        </div>
      </div>
    </header>
  );
}
