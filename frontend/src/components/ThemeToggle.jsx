import { Sun, Moon } from 'lucide-react';
import { useTheme } from '../context/ThemeContext';

export default function ThemeToggle() {
  const { darkMode, toggleTheme } = useTheme();
  return (
    <button
      onClick={toggleTheme}
      aria-label="Toggle theme"
      className="flex items-center gap-1.5 rounded-full border border-slate-200 dark:border-slate-700 bg-slate-100 dark:bg-slate-800 p-1.5 text-slate-700 dark:text-slate-200 transition hover:border-teal-500"
    >
      <span className={`rounded-full p-1 transition ${!darkMode ? 'bg-white text-amber-500 shadow-xs' : 'text-slate-400'}`}>
        <Sun className="h-4 w-4" />
      </span>
      <span className={`rounded-full p-1 transition ${darkMode ? 'bg-teal-600 text-white shadow-xs' : 'text-slate-400'}`}>
        <Moon className="h-4 w-4" />
      </span>
    </button>
  );
}