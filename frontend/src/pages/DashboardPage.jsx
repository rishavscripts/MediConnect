import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Pill, MapPin, CalendarCheck, CheckCircle2, FileText, Sparkles, Edit3 } from 'lucide-react';
import apiClient from '../api/apiClient';

const COMMON_CONDITIONS = ['Diabetes', 'Hypertension', 'Asthma', 'Thyroid', 'Heart Condition', 'None'];
const COMMON_ALLERGIES = ['Penicillin', 'Sulfa Drugs', 'NSAIDs / Aspirin', 'Peanuts', 'Lactose', 'None'];

export default function DashboardPage() {
  const [conditions, setConditions] = useState([]);
  const [allergies, setAllergies] = useState([]);
  const [customNotes, setCustomNotes] = useState('');
  const [isSaved, setIsSaved] = useState(false);
  const [isEditing, setIsEditing] = useState(true);

  useEffect(() => {
    const saved = localStorage.getItem('mc_medical_profile');
    if (saved) {
      const parsed = JSON.parse(saved);
      setConditions(parsed.conditions || []);
      setAllergies(parsed.allergies || []);
      setCustomNotes(parsed.customNotes || '');
      setIsSaved(true);
      setIsEditing(false);
    }
  }, []);

  const toggleItem = (list, setList, item) => {
    if (item === 'None') {
      setList(['None']);
      return;
    }
    const filtered = list.filter((i) => i !== 'None');
    setList(filtered.includes(item) ? filtered.filter((i) => i !== item) : [...filtered, item]);
  };

  const handleSaveProfile = async () => {
    const payload = {
      medicalConditions: conditions,
      allergies,
      customMedicalNotes: customNotes,
      isProfileComplete: true,
    };
    localStorage.setItem('mc_medical_profile', JSON.stringify({ conditions, allergies, customNotes }));
    setIsSaved(true);
    setIsEditing(false);

    try {
      await apiClient.put('/api/profile', payload);
    } catch {
      // Saved locally so UI works immediately even before backend starts
    }
  };

  return (
    <div className="space-y-6">
      <div className="flex flex-wrap items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold">Welcome to Your Health Hub</h1>
          <p className="text-sm text-slate-500 dark:text-slate-400">
            Manage your medical profile, run AI symptom checks, and locate nearby hospitals.
          </p>
        </div>
        {isSaved && !isEditing && (
          <button
            onClick={() => setIsEditing(true)}
            className="inline-flex items-center gap-2 text-xs font-semibold px-3.5 py-2 rounded-xl border border-teal-600 text-teal-600 dark:text-teal-400 hover:bg-teal-50 dark:hover:bg-teal-950/50"
          >
            <Edit3 className="h-3.5 w-3.5" /> Edit Medical History
          </button>
        )}
      </div>

      {/* First-Time Medical History Setup Card (with Custom Writing Area) */}
      {isEditing && (
        <div className="bg-white dark:bg-slate-900 border-2 border-teal-500/60 rounded-2xl p-6 shadow-md">
          <div className="flex items-center justify-between mb-4">
            <div className="flex items-center gap-2.5">
              <div className="bg-teal-100 dark:bg-teal-900/50 p-2 rounded-xl text-teal-600 dark:text-teal-400">
                <FileText className="h-5 w-5" />
              </div>
              <div>
                <h2 className="text-lg font-bold">Complete Your Medical Profile & History</h2>
                <p className="text-xs text-slate-500 dark:text-slate-400">
                  First-time setup: Groq AI uses this information to filter out unsafe medications and personalize your diet plan.
                </p>
              </div>
            </div>
          </div>

          <div className="grid lg:grid-cols-2 gap-6">
            <div className="space-y-4">
              <div>
                <label className="block text-xs font-bold uppercase text-slate-500 mb-2">Existing Conditions</label>
                <div className="flex flex-wrap gap-2">
                  {COMMON_CONDITIONS.map((item) => (
                    <button
                      key={item}
                      type="button"
                      onClick={() => toggleItem(conditions, setConditions, item)}
                      className={`px-3 py-1.5 rounded-lg text-xs font-semibold border transition ${
                        conditions.includes(item)
                          ? 'bg-teal-600 text-white border-teal-600'
                          : 'border-slate-200 dark:border-slate-700 hover:border-teal-500'
                      }`}
                    >
                      {conditions.includes(item) ? '✓ ' : '+ '}
                      {item}
                    </button>
                  ))}
                </div>
              </div>

              <div>
                <label className="block text-xs font-bold uppercase text-slate-500 mb-2">Known Drug or Food Allergies</label>
                <div className="flex flex-wrap gap-2">
                  {COMMON_ALLERGIES.map((item) => (
                    <button
                      key={item}
                      type="button"
                      onClick={() => toggleItem(allergies, setAllergies, item)}
                      className={`px-3 py-1.5 rounded-lg text-xs font-semibold border transition ${
                        allergies.includes(item)
                          ? 'bg-rose-600 text-white border-rose-600'
                          : 'border-slate-200 dark:border-slate-700 hover:border-rose-400'
                      }`}
                    >
                      {allergies.includes(item) ? '✓ ' : '+ '}
                      {item}
                    </button>
                  ))}
                </div>
              </div>
            </div>

            {/* Dedicated Free-Text Writing Section */}
            <div className="flex flex-col justify-between">
              <div>
                <label className="block text-xs font-bold uppercase text-slate-500 mb-2">
                  Custom Medical History & Notes (Write anything not listed above)
                </label>
                <textarea
                  rows={4}
                  value={customNotes}
                  onChange={(e) => setCustomNotes(e.target.value)}
                  placeholder="Write any past surgeries, ongoing prescriptions, family medical history, or specific sensitivities not mentioned in the tags..."
                  className="w-full p-3.5 rounded-xl border border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800/60 text-sm focus:outline-none focus:border-teal-500"
                />
              </div>

              <div className="flex justify-end mt-4">
                <button
                  onClick={handleSaveProfile}
                  className="inline-flex items-center gap-2 bg-teal-600 hover:bg-teal-700 text-white text-sm font-semibold px-5 py-2.5 rounded-xl transition shadow-sm"
                >
                  <CheckCircle2 className="h-4 w-4" /> Save Medical Profile
                </button>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* Quick Stats Row */}
      <div className="grid sm:grid-cols-3 gap-4">
        <div className="bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-2xl p-5">
          <div className="text-xs font-semibold text-slate-400 uppercase">Active Allergies Guarded</div>
          <div className="text-xl font-bold mt-1 text-rose-600 dark:text-rose-400">
            {allergies.length > 0 ? allergies.join(', ') : 'None recorded yet'}
          </div>
        </div>
        <div className="bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-2xl p-5">
          <div className="text-xs font-semibold text-slate-400 uppercase">Nearest Emergency Care</div>
          <div className="text-xl font-bold mt-1">Panihati State General Hospital</div>
          <div className="text-xs text-teal-600 mt-0.5">1.2 km away • Open 24/7</div>
        </div>
        <div className="bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-2xl p-5">
          <div className="text-xs font-semibold text-slate-400 uppercase">Weekly Dose Adherence</div>
          <div className="text-xl font-bold mt-1 text-teal-600 dark:text-teal-400">92% On Track</div>
        </div>
      </div>

      {/* 3 Core Service Modules */}
      <div className="grid md:grid-cols-3 gap-6">
        <Link
          to="/medications"
          className="group bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 hover:border-teal-500 rounded-2xl p-6 transition shadow-xs"
        >
          <div className="h-12 w-12 rounded-xl bg-teal-100 dark:bg-teal-900/50 text-teal-600 dark:text-teal-400 flex items-center justify-center mb-4">
            <Sparkles className="h-6 w-6" />
          </div>
          <h3 className="text-lg font-bold group-hover:text-teal-600 transition">Basic Medication AI</h3>
          <p className="text-sm text-slate-500 dark:text-slate-400 mt-2 mb-4">
            Enter symptoms to get allergy-checked OTC suggestions, diet plans, and auto-updated nearby hospital pins.
          </p>
          <span className="text-sm font-semibold text-teal-600 dark:text-teal-400">Analyze Symptoms →</span>
        </Link>

        <Link
          to="/hospitals"
          className="group bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 hover:border-teal-500 rounded-2xl p-6 transition shadow-xs"
        >
          <div className="h-12 w-12 rounded-xl bg-teal-100 dark:bg-teal-900/50 text-teal-600 dark:text-teal-400 flex items-center justify-center mb-4">
            <MapPin className="h-6 w-6" />
          </div>
          <h3 className="text-lg font-bold group-hover:text-teal-600 transition">Hospital & Doctor Locator</h3>
          <p className="text-sm text-slate-500 dark:text-slate-400 mt-2 mb-4">
            Search specialists by condition on an interactive OpenStreetMap with one-tap direct hospital calling.
          </p>
          <span className="text-sm font-semibold text-teal-600 dark:text-teal-400">Open Live Map →</span>
        </Link>

        <Link
          to="/assistant"
          className="group bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 hover:border-teal-500 rounded-2xl p-6 transition shadow-xs"
        >
          <div className="h-12 w-12 rounded-xl bg-teal-100 dark:bg-teal-900/50 text-teal-600 dark:text-teal-400 flex items-center justify-center mb-4">
            <CalendarCheck className="h-6 w-6" />
          </div>
          <h3 className="text-lg font-bold group-hover:text-teal-600 transition">Personal Medication Assistant</h3>
          <p className="text-sm text-slate-500 dark:text-slate-400 mt-2 mb-4">
            Manage daily Morning/Evening pill reminders, log taken or missed doses, and track doctor check-ups.
          </p>
          <span className="text-sm font-semibold text-teal-600 dark:text-teal-400">Manage Schedule →</span>
        </Link>
      </div>
    </div>
  );
}