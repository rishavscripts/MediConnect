import { NavLink, Outlet, useNavigate } from 'react-router-dom';
import { LayoutDashboard, Pill, MapPin, CalendarCheck, LogOut, HeartPulse } from 'lucide-react';
import ThemeToggle from './ThemeToggle';
import { supabase } from '../lib/supabaseClient';

const navItems = [
  { to: '/dashboard', label: 'Dashboard', icon: LayoutDashboard },
  { to: '/medications', label: 'Medication AI', icon: Pill },
  { to: '/hospitals', label: 'Hospital Map', icon: MapPin },
  { to: '/assistant', label: 'Medication Assistant', icon: CalendarCheck },
];

export default function AppLayout() {
  const navigate = useNavigate();

  const handleLogout = async () => {
    await supabase.auth.signOut();
    localStorage.removeItem('mc_demo_user');
    navigate('/');
  };

  return (
    <div className="min-h-screen flex bg-slate-50 text-slate-900 dark:bg-slate-950 dark:text-slate-100 transition-colors">
      {/* Sidebar */}
      <aside className="w-64 hidden md:flex flex-col bg-teal-950 text-white p-5 justify-between">
        <div>
          <div className="flex items-center gap-2.5 px-2 mb-8">
            <div className="bg-teal-500/20 p-2 rounded-xl border border-teal-400/30">
              <HeartPulse className="h-6 w-6 text-teal-400" />
            </div>
            <span className="text-xl font-bold tracking-tight">MediConnect</span>
          </div>

          <nav className="space-y-1.5">
            {navItems.map(({ to, label, icon: Icon }) => (
              <NavLink
                key={to}
                to={to}
                className={({ isActive }) =>
                  `flex items-center gap-3 px-3.5 py-2.5 rounded-xl text-sm font-medium transition ${
                    isActive
                      ? 'bg-teal-600 text-white shadow-sm'
                      : 'text-teal-100/75 hover:bg-teal-900/60 hover:text-white'
                  }`
                }
              >
                <Icon className="h-4 w-4" />
                {label}
              </NavLink>
            ))}
          </nav>
        </div>

        <button
          onClick={handleLogout}
          className="flex items-center gap-3 px-3.5 py-2.5 rounded-xl text-sm font-medium text-teal-200/80 hover:bg-red-500/20 hover:text-red-200 transition"
        >
          <LogOut className="h-4 w-4" />
          Sign Out
        </button>
      </aside>

      {/* Main Content */}
      <div className="flex-1 flex flex-col min-w-0">
        <header className="h-16 border-b border-slate-200 dark:border-slate-800 bg-white/80 dark:bg-slate-900/80 backdrop-blur-md px-6 flex items-center justify-between sticky top-0 z-20">
          <div className="flex items-center gap-2 md:hidden">
            <HeartPulse className="h-6 w-6 text-teal-600" />
            <span className="font-bold">MediConnect</span>
          </div>
          <div className="hidden md:block text-sm text-slate-500 dark:text-slate-400">
            AI-Assisted Patient Guidance & Hospital Navigation
          </div>
          <div className="flex items-center gap-4">
            <ThemeToggle />
            <div className="h-9 w-9 rounded-full bg-teal-600 text-white flex items-center justify-center font-semibold text-sm">
              RM
            </div>
          </div>
        </header>

        <main className="flex-1 p-6 max-w-7xl w-full mx-auto">
          <Outlet />
        </main>
      </div>
    </div>
  );
}