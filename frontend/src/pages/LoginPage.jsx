import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { HeartPulse } from 'lucide-react';
import { supabase, isSupabaseConfigured } from '../lib/supabaseClient';

export default function LoginPage() {
  const navigate = useNavigate();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const handleLogin = async (e) => {
    e.preventDefault();
    if (!isSupabaseConfigured) {
      navigate('/dashboard');
      return;
    }
    const { error: signInError } = await supabase.auth.signInWithPassword({ email, password });
    if (signInError) setError(signInError.message);
    else navigate('/dashboard');
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-slate-50 dark:bg-slate-950 p-6">
      <div className="w-full max-w-md bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-2xl p-8 shadow-sm">
        <div className="flex items-center gap-2 text-teal-600 font-bold text-xl mb-6">
          <HeartPulse className="h-6 w-6" /> MediConnect
        </div>
        <h1 className="text-2xl font-bold mb-2">Welcome Back</h1>
        <p className="text-sm text-slate-500 mb-6">Sign in to access your health dashboard.</p>
        {error && <div className="mb-4 p-3 rounded-xl bg-red-50 text-red-600 text-sm">{error}</div>}
        <form onSubmit={handleLogin} className="space-y-4">
          <input
            required
            type="email"
            placeholder="Email Address"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className="w-full px-3.5 py-2.5 rounded-xl border border-slate-200 dark:border-slate-700 bg-transparent text-sm"
          />
          <input
            required
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            className="w-full px-3.5 py-2.5 rounded-xl border border-slate-200 dark:border-slate-700 bg-transparent text-sm"
          />
          <button type="submit" className="w-full bg-teal-600 hover:bg-teal-700 text-white font-semibold py-3 rounded-xl">
            Sign In
          </button>
        </form>
        <p className="text-center text-sm text-slate-500 mt-6">
          New to MediConnect? <Link to="/signup" className="text-teal-600 font-semibold">Create Account</Link>
        </p>
      </div>
    </div>
  );
}