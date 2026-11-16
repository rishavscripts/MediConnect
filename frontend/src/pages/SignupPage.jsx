import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { HeartPulse, ArrowRight } from 'lucide-react';
import { supabase, isSupabaseConfigured } from '../lib/supabaseClient';

export default function SignupPage() {
  const navigate = useNavigate();
  const [form, setForm] = useState({ fullName: '', age: '', gender: 'Male', email: '', password: '' });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    if (!isSupabaseConfigured) {
      // Demo fallback before .env is wired
      localStorage.setItem('mc_demo_user', JSON.stringify({ ...form, isProfileComplete: false }));
      navigate('/dashboard');
      return;
    }

    const { error: signUpError } = await supabase.auth.signUp({
      email: form.email,
      password: form.password,
      options: {
        data: {
          full_name: form.fullName,
          age: Number(form.age),
          gender: form.gender,
        },
      },
    });

    setLoading(false);
    if (signUpError) {
      setError(signUpError.message);
    } else {
      navigate('/dashboard');
    }
  };

  return (
    <div className="min-h-screen grid lg:grid-cols-2 bg-slate-50 dark:bg-slate-950 text-slate-900 dark:text-slate-100">
      {/* Left Brand Panel */}
      <div className="hidden lg:flex flex-col justify-between bg-gradient-to-br from-teal-700 via-teal-800 to-teal-950 text-white p-12">
        <div className="flex items-center gap-2.5">
          <HeartPulse className="h-7 w-7 text-teal-300" />
          <span className="text-2xl font-bold">MediConnect</span>
        </div>
        <div>
          <h2 className="text-3xl font-bold mb-4">Join MediConnect Today</h2>
          <p className="text-teal-100/80 leading-relaxed">
            Create your account in seconds. You’ll customize your medical history, allergies, and custom health notes right inside your dashboard.
          </p>
        </div>
        <div className="text-xs text-teal-200/70">Protected by Supabase Auth & JWT Role Security</div>
      </div>

      {/* Right Streamlined Form */}
      <div className="flex items-center justify-center p-6 sm:p-12">
        <div className="w-full max-w-md bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-2xl p-8 shadow-sm">
          <div className="flex justify-between items-center mb-6">
            <h1 className="text-2xl font-bold">Create Account</h1>
            <Link to="/login" className="text-sm text-teal-600 font-medium hover:underline">
              Sign In
            </Link>
          </div>

          {error && <div className="mb-4 p-3 rounded-xl bg-red-50 dark:bg-red-950/50 text-red-600 text-sm">{error}</div>}

          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-xs font-semibold uppercase text-slate-500 mb-1">Full Name</label>
              <input
                required
                type="text"
                placeholder="Rishav Mitra"
                value={form.fullName}
                onChange={(e) => setForm({ ...form, fullName: e.target.value })}
                className="w-full px-3.5 py-2.5 rounded-xl border border-slate-200 dark:border-slate-700 bg-transparent text-sm focus:outline-none focus:border-teal-500"
              />
            </div>

            <div className="grid grid-cols-2 gap-3">
              <div>
                <label className="block text-xs font-semibold uppercase text-slate-500 mb-1">Age</label>
                <input
                  required
                  type="number"
                  min="1"
                  max="120"
                  placeholder="22"
                  value={form.age}
                  onChange={(e) => setForm({ ...form, age: e.target.value })}
                  className="w-full px-3.5 py-2.5 rounded-xl border border-slate-200 dark:border-slate-700 bg-transparent text-sm focus:outline-none focus:border-teal-500"
                />
              </div>
              <div>
                <label className="block text-xs font-semibold uppercase text-slate-500 mb-1">Gender</label>
                <select
                  value={form.gender}
                  onChange={(e) => setForm({ ...form, gender: e.target.value })}
                  className="w-full px-3.5 py-2.5 rounded-xl border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-900 text-sm focus:outline-none focus:border-teal-500"
                >
                  <option>Male</option>
                  <option>Female</option>
                  <option>Other</option>
                </select>
              </div>
            </div>

            <div>
              <label className="block text-xs font-semibold uppercase text-slate-500 mb-1">Email Address</label>
              <input
                required
                type="email"
                placeholder="you@example.com"
                value={form.email}
                onChange={(e) => setForm({ ...form, email: e.target.value })}
                className="w-full px-3.5 py-2.5 rounded-xl border border-slate-200 dark:border-slate-700 bg-transparent text-sm focus:outline-none focus:border-teal-500"
              />
            </div>

            <div>
              <label className="block text-xs font-semibold uppercase text-slate-500 mb-1">Password</label>
              <input
                required
                type="password"
                minLength={6}
                placeholder="••••••••"
                value={form.password}
                onChange={(e) => setForm({ ...form, password: e.target.value })}
                className="w-full px-3.5 py-2.5 rounded-xl border border-slate-200 dark:border-slate-700 bg-transparent text-sm focus:outline-none focus:border-teal-500"
              />
            </div>

            <button
              disabled={loading}
              type="submit"
              className="w-full bg-teal-600 hover:bg-teal-700 text-white font-semibold py-3 rounded-xl flex items-center justify-center gap-2 transition"
            >
              {loading ? 'Creating Account...' : 'Create Account'} <ArrowRight className="h-4 w-4" />
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}