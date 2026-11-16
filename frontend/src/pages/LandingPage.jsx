import { useState } from 'react';
import { Link } from 'react-router-dom';
import {
  HeartPulse,
  Sparkles,
  MapPin,
  CalendarCheck,
  ArrowRight,
  ShieldCheck,
  Activity,
  Menu,
  X,
  Mail,
  Phone,
  AlertCircle,
} from 'lucide-react';
import ThemeToggle from '../components/ThemeToggle';

const quickExamples = {
  'Headache & mild fever': {
    action: 'Rest, hydration, OTC Paracetamol (check liver/allergy profile)',
    specialist: 'General Physician',
    urgency: 'Low — Monitor for 24h',
  },
  'Chest tightness & shortness of breath': {
    action: 'Seek immediate emergency evaluation; avoid exertion',
    specialist: 'Cardiology / Emergency Care',
    urgency: 'High — Immediate Hospital Visit',
  },
  'Acidity & stomach cramps': {
    action: 'Light bland meals, hydration, antacid guidance',
    specialist: 'Gastroenterology',
    urgency: 'Low to Moderate',
  },
};

export default function LandingPage() {
  const [sampleQuery, setSampleQuery] = useState('Headache & mild fever');
  const [activeTab, setActiveTab] = useState('ai');
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);

  const preview = quickExamples[sampleQuery] || quickExamples['Headache & mild fever'];

  return (
    <div id="top" className="min-h-screen flex flex-col bg-slate-50 text-slate-900 dark:bg-slate-950 dark:text-slate-100 transition-colors">
      {/* Sticky Top Header */}
      <header className="sticky top-0 z-30 border-b border-slate-200/80 dark:border-slate-800/80 bg-white/85 dark:bg-slate-950/85 backdrop-blur-md">
        <div className="max-w-7xl mx-auto px-6 h-20 flex items-center justify-between">
          {/* Brand Logo */}
          <a href="#top" className="flex items-center gap-2.5">
            <div className="bg-teal-600 p-2 rounded-xl text-white shadow-xs">
              <HeartPulse className="h-6 w-6" />
            </div>
            <span className="text-xl font-bold tracking-tight">MediConnect</span>
          </a>

          {/* Desktop Navigation Links */}
          <nav className="hidden md:flex items-center gap-8 text-sm font-medium text-slate-600 dark:text-slate-300">
            <a href="#features" className="hover:text-teal-600 dark:hover:text-teal-400 transition">
              Services
            </a>
            <a href="#how-it-works" className="hover:text-teal-600 dark:hover:text-teal-400 transition">
              How It Works
            </a>
            <a href="#about" className="hover:text-teal-600 dark:hover:text-teal-400 transition">
              About
            </a>
            <a href="#contact" className="hover:text-teal-600 dark:hover:text-teal-400 transition">
              Contact
            </a>
          </nav>

          {/* Desktop Actions */}
          <div className="hidden md:flex items-center gap-4">
            <ThemeToggle />
            <Link
              to="/login"
              className="text-sm font-medium text-slate-600 dark:text-slate-300 hover:text-teal-600 transition"
            >
              Sign In
            </Link>
            <Link
              to="/signup"
              className="bg-teal-600 hover:bg-teal-700 text-white text-sm font-semibold px-5 py-2.5 rounded-xl transition shadow-sm"
            >
              Get Started
            </Link>
          </div>

          {/* Mobile Menu Toggle */}
          <div className="flex items-center gap-3 md:hidden">
            <ThemeToggle />
            <button
              onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
              aria-label="Toggle Menu"
              className="p-2 rounded-xl border border-slate-200 dark:border-slate-800 text-slate-700 dark:text-slate-200"
            >
              {mobileMenuOpen ? <X className="h-5 w-5" /> : <Menu className="h-5 w-5" />}
            </button>
          </div>
        </div>

        {/* Mobile Dropdown Menu */}
        {mobileMenuOpen && (
          <div className="md:hidden border-t border-slate-200 dark:border-slate-800 bg-white dark:bg-slate-900 px-6 py-4 space-y-3">
            <a href="#features" onClick={() => setMobileMenuOpen(false)} className="block text-sm font-medium py-1">
              Services
            </a>
            <a href="#how-it-works" onClick={() => setMobileMenuOpen(false)} className="block text-sm font-medium py-1">
              How It Works
            </a>
            <a href="#about" onClick={() => setMobileMenuOpen(false)} className="block text-sm font-medium py-1">
              About
            </a>
            <a href="#contact" onClick={() => setMobileMenuOpen(false)} className="block text-sm font-medium py-1">
              Contact
            </a>
            <div className="pt-3 border-t border-slate-200 dark:border-slate-800 flex gap-3">
              <Link
                to="/login"
                className="flex-1 text-center py-2.5 rounded-xl border border-slate-200 dark:border-slate-700 text-sm font-semibold"
              >
                Sign In
              </Link>
              <Link
                to="/signup"
                className="flex-1 text-center py-2.5 rounded-xl bg-teal-600 text-white text-sm font-semibold"
              >
                Get Started
              </Link>
            </div>
          </div>
        )}
      </header>

      {/* Main Content */}
      <main className="flex-1">
        {/* Hero Section with Interactive Sandbox */}
        <section className="max-w-7xl mx-auto px-6 py-12 lg:py-20 grid lg:grid-cols-2 gap-12 items-center">
          <div>
            <span className="inline-flex items-center gap-2 px-3.5 py-1.5 rounded-full text-xs font-semibold bg-teal-100 text-teal-800 dark:bg-teal-900/50 dark:text-teal-300 mb-6">
              <Sparkles className="h-3.5 w-3.5" /> Powered by Groq AI & OpenStreetMap
            </span>
            <h1 className="text-4xl lg:text-5xl font-extrabold tracking-tight leading-tight mb-5">
              Your Personal AI Health & <span className="text-teal-600 dark:text-teal-400">Hospital Companion</span>
            </h1>
            <p className="text-lg text-slate-600 dark:text-slate-400 mb-8 leading-relaxed">
              Cross-check symptoms against your personal medical history, locate nearest specialized hospitals on a live map, and stay on top of daily medication schedules.
            </p>

            <div className="flex flex-wrap gap-4 mb-8">
              <Link
                to="/signup"
                className="inline-flex items-center gap-2 bg-teal-600 hover:bg-teal-700 text-white font-semibold px-6 py-3.5 rounded-xl transition shadow-md"
              >
                Create Free Patient Profile <ArrowRight className="h-4 w-4" />
              </Link>
              <Link
                to="/dashboard"
                className="inline-flex items-center gap-2 bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 hover:border-teal-500 font-semibold px-6 py-3.5 rounded-xl transition"
              >
                Explore Live Demo
              </Link>
            </div>

            <div className="flex flex-wrap items-center gap-6 text-xs font-medium text-slate-500 dark:text-slate-400">
              <span className="flex items-center gap-1.5">
                <ShieldCheck className="h-4 w-4 text-teal-600" /> Allergy-Aware AI
              </span>
              <span className="flex items-center gap-1.5">
                <MapPin className="h-4 w-4 text-teal-600" /> Real-Time OSM Routing
              </span>
              <span className="flex items-center gap-1.5">
                <Activity className="h-4 w-4 text-teal-600" /> Adherence Analytics
              </span>
            </div>
          </div>

          {/* Interactive Live Symptom Sandbox Card */}
          <div className="bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-2xl p-6 shadow-xl">
            <div className="flex items-center justify-between mb-4">
              <span className="text-sm font-bold flex items-center gap-2">
                <Sparkles className="h-4 w-4 text-teal-600" /> Interactive Symptom Quick-Check
              </span>
              <span className="text-xs px-2.5 py-1 rounded-full bg-emerald-100 text-emerald-700 dark:bg-emerald-900/40 dark:text-emerald-300 font-medium">
                Click a symptom
              </span>
            </div>

            <div className="flex flex-wrap gap-2 mb-4">
              {Object.keys(quickExamples).map((item) => (
                <button
                  key={item}
                  onClick={() => setSampleQuery(item)}
                  className={`text-xs px-3 py-1.5 rounded-lg font-medium transition ${
                    sampleQuery === item
                      ? 'bg-teal-600 text-white'
                      : 'bg-slate-100 dark:bg-slate-800 text-slate-600 dark:text-slate-300 hover:bg-teal-50 dark:hover:bg-slate-700'
                  }`}
                >
                  {item}
                </button>
              ))}
            </div>

            <div className="p-4 rounded-xl bg-slate-50 dark:bg-slate-800/70 border border-slate-200/80 dark:border-slate-700 space-y-3">
              <div>
                <div className="text-xs uppercase tracking-wider text-slate-400 font-semibold">Suggested Guidance</div>
                <div className="text-sm font-medium mt-0.5">{preview.action}</div>
              </div>
              <div className="grid grid-cols-2 gap-3 pt-2 border-t border-slate-200 dark:border-slate-700">
                <div>
                  <div className="text-xs text-slate-400">Matched Department</div>
                  <div className="text-sm font-semibold text-teal-600 dark:text-teal-400">{preview.specialist}</div>
                </div>
                <div>
                  <div className="text-xs text-slate-400">Triage Level</div>
                  <div className="text-sm font-semibold">{preview.urgency}</div>
                </div>
              </div>
            </div>
          </div>
        </section>

        {/* Interactive Module Showcase Tabs */}
        <section id="features" className="max-w-7xl mx-auto px-6 py-12">
          <div className="text-center mb-8">
            <h2 className="text-2xl lg:text-3xl font-bold">Three Core Healthcare Modules</h2>
            <p className="text-sm text-slate-500 dark:text-slate-400 mt-1">
              Click each module below to explore how MediConnect supports patients end-to-end.
            </p>
          </div>

          <div className="flex flex-wrap justify-center gap-3 mb-8">
            {[
              { id: 'ai', label: '1. Medication AI & Diet', icon: Sparkles },
              { id: 'map', label: '2. Hospital & Doctor Map', icon: MapPin },
              { id: 'tracker', label: '3. Personal Assistant', icon: CalendarCheck },
            ].map(({ id, label, icon: Icon }) => (
              <button
                key={id}
                onClick={() => setActiveTab(id)}
                className={`flex items-center gap-2 px-4 py-2.5 rounded-xl text-sm font-semibold transition ${
                  activeTab === id
                    ? 'bg-teal-600 text-white shadow-sm'
                    : 'bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 text-slate-600 dark:text-slate-300'
                }`}
              >
                <Icon className="h-4 w-4" /> {label}
              </button>
            ))}
          </div>

          <div className="bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-2xl p-8 text-center max-w-3xl mx-auto shadow-xs">
            {activeTab === 'ai' && (
              <>
                <h3 className="text-xl font-bold mb-2">Allergy-Safe OTC & Dietary Recommendations</h3>
                <p className="text-slate-600 dark:text-slate-400 text-sm leading-relaxed">
                  Groq AI cross-references your symptoms with your saved allergies and custom medical notes to recommend safe OTC relief, foods to eat/avoid, and automatically pins matching specialists on the live map.
                </p>
              </>
            )}
            {activeTab === 'map' && (
              <>
                <h3 className="text-xl font-bold mb-2">Live OpenStreetMap & Leaflet Hospital Finder</h3>
                <p className="text-slate-600 dark:text-slate-400 text-sm leading-relaxed">
                  Auto-detect your GPS coordinates, filter by specialization (Cardiology, General Medicine, Orthopedics), view distances, and call emergency desks in one tap.
                </p>
              </>
            )}
            {activeTab === 'tracker' && (
              <>
                <h3 className="text-xl font-bold mb-2">Daily Pill Timeline & Adherence Analytics</h3>
                <p className="text-slate-600 dark:text-slate-400 text-sm leading-relaxed">
                  Log Morning, Afternoon, and Evening doses as Taken or Missed, track weekly adherence percentages, and manage upcoming doctor check-up appointments.
                </p>
              </>
            )}
          </div>
        </section>

        {/* How It Works Section */}
        <section id="how-it-works" className="max-w-7xl mx-auto px-6 py-12">
          <div className="grid md:grid-cols-3 gap-6">
            {[
              {
                step: '01',
                title: 'Create Account & Medical Profile',
                desc: 'Sign up quickly and record your allergies, conditions, and custom medical notes inside your dashboard.',
              },
              {
                step: '02',
                title: 'Analyze Symptoms & Auto-Locate Care',
                desc: 'Get tailored OTC guidance, diet advice, and instant hospital pins matched to your condition.',
              },
              {
                step: '03',
                title: 'Track Daily Medication Schedules',
                desc: 'Follow your personalized Morning/Evening dose timeline and monitor adherence progress.',
              },
            ].map((item) => (
              <div
                key={item.step}
                className="bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-2xl p-6"
              >
                <span className="text-xs font-extrabold px-2.5 py-1 rounded-lg bg-teal-100 text-teal-800 dark:bg-teal-900/50 dark:text-teal-300">
                  STEP {item.step}
                </span>
                <h3 className="text-lg font-bold mt-4 mb-2">{item.title}</h3>
                <p className="text-sm text-slate-600 dark:text-slate-400 leading-relaxed">{item.desc}</p>
              </div>
            ))}
          </div>
        </section>
      </main>

      {/* Footer */}
      <footer id="about" className="border-t border-slate-200 dark:border-slate-800 bg-white dark:bg-slate-900 mt-12">
        <div className="max-w-7xl mx-auto px-6 py-12 grid md:grid-cols-4 gap-8">
          {/* Brand & Mission */}
          <div className="md:col-span-2 space-y-3">
            <div className="flex items-center gap-2.5">
              <div className="bg-teal-600 p-1.5 rounded-lg text-white">
                <HeartPulse className="h-5 w-5" />
              </div>
              <span className="text-lg font-bold">MediConnect</span>
            </div>
            <p className="text-sm text-slate-500 dark:text-slate-400 max-w-md leading-relaxed">
              An AI-assisted patient guidance and hospital navigation platform combining personalized medical history awareness, OpenStreetMap routing, and daily medication adherence tracking.
            </p>
            <div className="flex items-start gap-2 p-3 rounded-xl bg-amber-50 dark:bg-amber-950/30 border border-amber-200/70 dark:border-amber-900/50 text-xs text-amber-800 dark:text-amber-300 max-w-md">
              <AlertCircle className="h-4 w-4 shrink-0 mt-0.5" />
              <span>
                <strong>Medical Disclaimer:</strong> MediConnect provides informational guidance only and does not replace professional medical diagnosis or emergency care.
              </span>
            </div>
          </div>

          {/* Quick Platform Links */}
          <div>
            <h4 className="text-xs font-bold uppercase tracking-wider text-slate-400 mb-3">Platform</h4>
            <ul className="space-y-2 text-sm text-slate-600 dark:text-slate-300">
              <li>
                <Link to="/dashboard" className="hover:text-teal-600 transition">
                  Patient Dashboard
                </Link>
              </li>
              <li>
                <Link to="/medications" className="hover:text-teal-600 transition">
                  Medication AI & Diet
                </Link>
              </li>
              <li>
                <Link to="/hospitals" className="hover:text-teal-600 transition">
                  Hospital & Doctor Map
                </Link>
              </li>
              <li>
                <Link to="/assistant" className="hover:text-teal-600 transition">
                  Personal Assistant
                </Link>
              </li>
            </ul>
          </div>

          {/* Contact & Support */}
          <div id="contact">
            <h4 className="text-xs font-bold uppercase tracking-wider text-slate-400 mb-3">Support & Contact</h4>
            <ul className="space-y-2.5 text-sm text-slate-600 dark:text-slate-300">
              <li className="flex items-center gap-2">
                <Mail className="h-4 w-4 text-teal-600" />
                <span>rishavscripts@gmail.com</span>
              </li>
              <li className="flex items-center gap-2">
                <Phone className="h-4 w-4 text-teal-600" />
                <span>Emergency Helpline: 112 / 108</span>
              </li>
              <li className="text-xs text-slate-400 pt-1">Kolkata, West Bengal, India</li>
            </ul>
          </div>
        </div>

        {/* Bottom Copyright Bar */}
        <div className="border-t border-slate-200 dark:border-slate-800 py-5 px-6">
          <div className="max-w-7xl mx-auto flex flex-col sm:flex-row items-center justify-between gap-3 text-xs text-slate-500 dark:text-slate-400">
            <span>© {new Date().getFullYear()} MediConnect Healthcare Assistant. All rights reserved.</span>
            <span>Built with React, Spring Boot, Supabase & Groq AI</span>
          </div>
        </div>
      </footer>
    </div>
  );
}