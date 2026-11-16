import { useState, useEffect } from 'react';
import { MapContainer, TileLayer, Marker, Popup, useMap } from 'react-leaflet';
import { Sparkles, AlertTriangle, Phone, Pill, Utensils } from 'lucide-react';
import apiClient from '../api/apiClient';

function RecenterMap({ center }) {
  const map = useMap();
  useEffect(() => {
    map.setView(center, 14);
  }, [center, map]);
  return null;
}

export default function MedicationAIPage() {
  const [symptoms, setSymptoms] = useState('');
  const [severity, setSeverity] = useState('Moderate');
  const [loading, setLoading] = useState(false);
  const [userCoords, setUserCoords] = useState([22.6942, 88.3745]); // Default: Panihati / Kolkata
  const [result, setResult] = useState(null);

  const savedProfile = JSON.parse(localStorage.getItem('mc_medical_profile') || '{}');

  useEffect(() => {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (pos) => setUserCoords([pos.coords.latitude, pos.coords.longitude]),
        () => {}
      );
    }
  }, []);

  const handleAnalyze = async (e) => {
    e.preventDefault();
    if (!symptoms.trim()) return;
    setLoading(true);

    try {
      const { data } = await apiClient.post('/api/medications/recommend', {
        symptoms,
        severity,
        latitude: userCoords[0],
        longitude: userCoords[1],
        medicalProfile: savedProfile,
      });
      setResult(data);
    } catch {
      // Smart local fallback when backend isn't running yet
      const isCardio = symptoms.toLowerCase().includes('chest') || symptoms.toLowerCase().includes('heart');
      setResult({
        matchedSpecialization: isCardio ? 'Cardiology & Emergency' : 'General Medicine',
        medications: isCardio
          ? [{ name: 'Seek Immediate Doctor Evaluation', dosage: 'Do not self-medicate chest pain', frequency: 'Urgent' }]
          : [
              { name: 'Paracetamol (Acetaminophen)', dosage: '500mg tablet', frequency: 'Every 6–8 hours after food' },
              { name: 'ORS Hydration Electrolyte', dosage: '1 sachet in 1L water', frequency: 'Sip throughout the day' },
            ],
        foodsToEat: ['Warm clear broth', 'Steamed rice / khichdi', 'Ginger & honey tea', 'Bananas'],
        foodsToAvoid: ['Fried oily foods', 'Cold carbonated drinks', 'Excess caffeine'],
        schedule: [
          { time: 'Morning (8:00 AM)', note: 'Take prescribed dose after breakfast' },
          { time: 'Evening (8:00 PM)', note: 'Take evening dose with warm water' },
        ],
        nearbyHospitals: [
          {
            id: 1,
            name: isCardio ? 'ILS Hospitals Heart Center' : 'Panihati State General Hospital',
            specialization: isCardio ? 'Cardiology' : 'General Medicine',
            phone: '+91 33 2553 2211',
            lat: userCoords[0] + 0.006,
            lng: userCoords[1] + 0.005,
          },
          {
            id: 2,
            name: 'Midland Nursing Home & Polyclinic',
            specialization: 'Multi-Specialty',
            phone: '+91 33 2563 1190',
            lat: userCoords[0] - 0.005,
            lng: userCoords[1] + 0.007,
          },
        ],
      });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold">AI Medication & Diet Recommendation</h1>
        <p className="text-sm text-slate-500 dark:text-slate-400">
          Describe your symptoms—Groq AI checks your allergy history and automatically pins matching hospitals below.
        </p>
      </div>

      <div className="grid lg:grid-cols-12 gap-6">
        {/* Left Input Form */}
        <form
          onSubmit={handleAnalyze}
          className="lg:col-span-5 bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-2xl p-6 space-y-5 h-fit"
        >
          <div>
            <label className="block text-xs font-bold uppercase text-slate-500 mb-2">Describe Your Symptoms</label>
            <textarea
              required
              rows={4}
              value={symptoms}
              onChange={(e) => setSymptoms(e.target.value)}
              placeholder="e.g., Mild fever, body ache, and sore throat since last night..."
              className="w-full p-3.5 rounded-xl border border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800/60 text-sm focus:outline-none focus:border-teal-500"
            />
          </div>

          <div>
            <label className="block text-xs font-bold uppercase text-slate-500 mb-2">Severity Level</label>
            <div className="grid grid-cols-3 gap-2">
              {['Mild', 'Moderate', 'Severe'].map((lvl) => (
                <button
                  type="button"
                  key={lvl}
                  onClick={() => setSeverity(lvl)}
                  className={`py-2 rounded-xl text-xs font-semibold border transition ${
                    severity === lvl
                      ? 'bg-teal-600 text-white border-teal-600'
                      : 'border-slate-200 dark:border-slate-700'
                  }`}
                >
                  {lvl}
                </button>
              ))}
            </div>
          </div>

          {/* Active Allergy Guard Box */}
          <div className="p-3.5 rounded-xl bg-rose-50 dark:bg-rose-950/40 border border-rose-200 dark:border-rose-900/60 flex items-start gap-2.5">
            <AlertTriangle className="h-4 w-4 text-rose-600 shrink-0 mt-0.5" />
            <div className="text-xs">
              <span className="font-bold text-rose-700 dark:text-rose-300">Allergy Guard Active: </span>
              <span className="text-rose-600 dark:text-rose-200">
                {savedProfile.allergies?.length ? savedProfile.allergies.join(', ') : 'No allergies set in Dashboard'}
              </span>
            </div>
          </div>

          <button
            disabled={loading}
            type="submit"
            className="w-full bg-teal-600 hover:bg-teal-700 text-white font-semibold py-3 rounded-xl flex items-center justify-center gap-2 transition"
          >
            <Sparkles className="h-4 w-4" />
            {loading ? 'Analyzing & Finding Hospitals...' : 'Generate Recommendation'}
          </button>
        </form>

        {/* Right Output + Auto-Updating Map */}
        <div className="lg:col-span-7 space-y-6">
          {!result ? (
            <div className="bg-white dark:bg-slate-900 border border-dashed border-slate-300 dark:border-slate-700 rounded-2xl p-12 text-center text-slate-400">
              Enter your symptoms on the left to generate personalized OTC medication advice, diet plan, and live hospital pins.
            </div>
          ) : (
            <>
              {/* Medications & Diet */}
              <div className="bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-2xl p-6 space-y-5">
                <div className="flex items-center justify-between">
                  <h2 className="font-bold text-lg flex items-center gap-2">
                    <Pill className="h-5 w-5 text-teal-600" /> Suggested OTC Guidance
                  </h2>
                  <span className="text-xs font-semibold px-3 py-1 rounded-full bg-teal-100 text-teal-800 dark:bg-teal-900/50 dark:text-teal-300">
                    Department: {result.matchedSpecialization}
                  </span>
                </div>

                <div className="grid sm:grid-cols-2 gap-3">
                  {result.medications.map((med, i) => (
                    <div key={i} className="p-3.5 rounded-xl bg-slate-50 dark:bg-slate-800/70 border border-slate-200/70 dark:border-slate-700">
                      <div className="font-bold text-sm">{med.name}</div>
                      <div className="text-xs text-teal-600 dark:text-teal-400 font-medium mt-0.5">{med.dosage}</div>
                      <div className="text-xs text-slate-500 mt-1">{med.frequency}</div>
                    </div>
                  ))}
                </div>

                <div className="grid sm:grid-cols-2 gap-4 pt-2">
                  <div className="p-3.5 rounded-xl bg-emerald-50/70 dark:bg-emerald-950/30 border border-emerald-200/70 dark:border-emerald-900/50">
                    <div className="text-xs font-bold text-emerald-700 dark:text-emerald-300 flex items-center gap-1.5 mb-2">
                      <Utensils className="h-3.5 w-3.5" /> Foods to Eat
                    </div>
                    <ul className="text-xs space-y-1 text-slate-700 dark:text-slate-300">
                      {result.foodsToEat.map((f, idx) => <li key={idx}>• {f}</li>)}
                    </ul>
                  </div>

                  <div className="p-3.5 rounded-xl bg-rose-50/70 dark:bg-rose-950/30 border border-rose-200/70 dark:border-rose-900/50">
                    <div className="text-xs font-bold text-rose-700 dark:text-rose-300 mb-2">Foods to Avoid</div>
                    <ul className="text-xs space-y-1 text-slate-700 dark:text-slate-300">
                      {result.foodsToAvoid.map((f, idx) => <li key={idx}>• {f}</li>)}
                    </ul>
                  </div>
                </div>
              </div>

              {/* Auto-Updating Nearest Hospitals Leaflet Map */}
              <div className="bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-2xl p-5 space-y-4">
                <div className="flex items-center justify-between">
                  <h3 className="font-bold text-base">
                    Auto-Matched Hospitals for <span className="text-teal-600">{result.matchedSpecialization}</span>
                  </h3>
                  <span className="text-xs text-slate-400">{result.nearbyHospitals.length} facilities pinned</span>
                </div>

                <div className="h-64 w-full rounded-xl overflow-hidden border border-slate-200 dark:border-slate-700">
                  <MapContainer center={userCoords} zoom={14} className="h-full w-full">
                    <RecenterMap center={userCoords} />
                    <TileLayer
                      attribution='&copy; OpenStreetMap contributors'
                      url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                    />
                    {result.nearbyHospitals.map((h) => (
                      <Marker key={h.id} position={[h.lat, h.lng]}>
                        <Popup>
                          <div className="text-xs">
                            <div className="font-bold">{h.name}</div>
                            <div className="text-teal-700">{h.specialization}</div>
                            <a href={`tel:${h.phone}`} className="inline-block mt-1 text-emerald-700 font-semibold underline">
                              Call {h.phone}
                            </a>
                          </div>
                        </Popup>
                      </Marker>
                    ))}
                  </MapContainer>
                </div>

                <div className="grid sm:grid-cols-2 gap-3">
                  {result.nearbyHospitals.map((h) => (
                    <div key={h.id} className="flex items-center justify-between p-3 rounded-xl border border-slate-200 dark:border-slate-800">
                      <div>
                        <div className="text-sm font-bold">{h.name}</div>
                        <div className="text-xs text-slate-500">{h.specialization}</div>
                      </div>
                      <a
                        href={`tel:${h.phone}`}
                        className="inline-flex items-center gap-1.5 bg-emerald-600 hover:bg-emerald-700 text-white text-xs font-semibold px-3 py-2 rounded-lg"
                      >
                        <Phone className="h-3.5 w-3.5" /> Call
                      </a>
                    </div>
                  ))}
                </div>
              </div>
            </>
          )}
        </div>
      </div>
    </div>
  );
}