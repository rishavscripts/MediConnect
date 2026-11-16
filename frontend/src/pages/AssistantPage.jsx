import { useState } from 'react';
import { Plus, Check, X, Calendar } from 'lucide-react';

export default function AssistantPage() {
  const [doses, setDoses] = useState([
    { id: 1, time: 'Morning (8:00 AM)', name: 'Vitamin D3 (1000 IU)', note: 'After breakfast', status: 'TAKEN' },
    { id: 2, time: 'Afternoon (1:30 PM)', name: 'B-Complex Capsule', note: 'With lunch', status: 'PENDING' },
    { id: 3, time: 'Evening (8:30 PM)', name: 'Cetirizine (10mg)', note: 'Before sleep', status: 'PENDING' },
  ]);
  const [newMed, setNewMed] = useState({ time: 'Morning (8:00 AM)', name: '', note: '' });

  const updateStatus = (id, status) => {
    setDoses((prev) => prev.map((d) => (d.id === id ? { ...d, status } : d)));
  };

  const addMedicine = (e) => {
    e.preventDefault();
    if (!newMed.name.trim()) return;
    setDoses([...doses, { id: Date.now(), ...newMed, status: 'PENDING' }]);
    setNewMed({ time: 'Morning (8:00 AM)', name: '', note: '' });
  };

  const takenCount = doses.filter((d) => d.status === 'TAKEN').length;
  const adherence = doses.length ? Math.round((takenCount / doses.length) * 100) : 0;

  return (
    <div className="space-y-6">
      <div className="flex flex-wrap items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold">Personal Medication Assistant</h1>
          <p className="text-sm text-slate-500 dark:text-slate-400">Track daily doses and upcoming doctor check-ups.</p>
        </div>
        <div className="bg-teal-600 text-white px-4 py-2 rounded-xl text-sm font-bold">
          Today’s Adherence: {adherence}% ({takenCount}/{doses.length})
        </div>
      </div>

      <div className="grid lg:grid-cols-12 gap-6">
        {/* Daily Dose Timeline */}
        <div className="lg:col-span-8 space-y-3">
          {doses.map((dose) => (
            <div
              key={dose.id}
              className="bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-2xl p-4 flex flex-wrap items-center justify-between gap-4"
            >
              <div>
                <span className="text-xs font-bold uppercase text-teal-600 dark:text-teal-400">{dose.time}</span>
                <h3 className="font-bold text-base">{dose.name}</h3>
                <p className="text-xs text-slate-500">{dose.note}</p>
              </div>

              <div className="flex items-center gap-2">
                <button
                  onClick={() => updateStatus(dose.id, 'TAKEN')}
                  className={`inline-flex items-center gap-1 px-3 py-1.5 rounded-lg text-xs font-semibold transition ${
                    dose.status === 'TAKEN'
                      ? 'bg-emerald-600 text-white'
                      : 'bg-slate-100 dark:bg-slate-800 text-slate-600 dark:text-slate-300'
                  }`}
                >
                  <Check className="h-3.5 w-3.5" /> Taken
                </button>
                <button
                  onClick={() => updateStatus(dose.id, 'MISSED')}
                  className={`inline-flex items-center gap-1 px-3 py-1.5 rounded-lg text-xs font-semibold transition ${
                    dose.status === 'MISSED'
                      ? 'bg-rose-600 text-white'
                      : 'bg-slate-100 dark:bg-slate-800 text-slate-600 dark:text-slate-300'
                  }`}
                >
                  <X className="h-3.5 w-3.5" /> Missed
                </button>
              </div>
            </div>
          ))}
        </div>

        {/* Add Reminder & Upcoming Checkups */}
        <div className="lg:col-span-4 space-y-6">
          <form
            onSubmit={addMedicine}
            className="bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-2xl p-5 space-y-3"
          >
            <h3 className="font-bold text-base">Add Medicine Reminder</h3>
            <select
              value={newMed.time}
              onChange={(e) => setNewMed({ ...newMed, time: e.target.value })}
              className="w-full px-3 py-2 rounded-xl border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-900 text-sm"
            >
              <option>Morning (8:00 AM)</option>
              <option>Afternoon (1:30 PM)</option>
              <option>Evening (8:30 PM)</option>
            </select>
            <input
              required
              type="text"
              placeholder="Medicine name & dosage"
              value={newMed.name}
              onChange={(e) => setNewMed({ ...newMed, name: e.target.value })}
              className="w-full px-3 py-2 rounded-xl border border-slate-200 dark:border-slate-700 bg-transparent text-sm"
            />
            <input
              type="text"
              placeholder="Instructions (e.g. After meal)"
              value={newMed.note}
              onChange={(e) => setNewMed({ ...newMed, note: e.target.value })}
              className="w-full px-3 py-2 rounded-xl border border-slate-200 dark:border-slate-700 bg-transparent text-sm"
            />
            <button type="submit" className="w-full bg-teal-600 hover:bg-teal-700 text-white text-sm font-semibold py-2.5 rounded-xl flex items-center justify-center gap-1.5">
              <Plus className="h-4 w-4" /> Add to Timeline
            </button>
          </form>

          <div className="bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-2xl p-5 space-y-3">
            <h3 className="font-bold text-base flex items-center gap-2">
              <Calendar className="h-4 w-4 text-teal-600" /> Upcoming Check-Ups
            </h3>
            <div className="p-3 rounded-xl bg-slate-50 dark:bg-slate-800/60 text-xs space-y-1">
              <div className="font-bold text-sm">Dr. Ananya Sen (General Physician)</div>
              <div className="text-teal-600 dark:text-teal-400 font-medium">Nov 22, 2026 • 10:30 AM</div>
              <div className="text-slate-500">Apollo Clinic, Barrackpore</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}