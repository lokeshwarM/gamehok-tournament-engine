import type { Metadata } from 'next';
import { Inter } from 'next/font/google';
import './globals.css';
import { Navbar } from '@/components/layout/Navbar';

const inter = Inter({ subsets: ['latin'], variable: '--font-inter' });

export const metadata: Metadata = {
  title: 'Gamehok Tournament Engine',
  description: 'Production-grade esports tournament orchestration platform',
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en" className="dark">
      <body className={`${inter.variable} min-h-screen bg-background font-sans text-foreground antialiased selection:bg-primary/30 flex flex-col`}>
        <Navbar />
        {children}
      </body>
    </html>
  );
}
