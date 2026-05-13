import { ReactNode } from 'react';
import { Sidebar } from './Sidebar';

export function DashboardLayout({ children }: { children: ReactNode }) {
  return (
    <div className="flex flex-1">
      <Sidebar />
      <main className="flex-1 overflow-y-auto bg-background p-6">
        <div className="mx-auto max-w-7xl">
          {children}
        </div>
      </main>
    </div>
  );
}
