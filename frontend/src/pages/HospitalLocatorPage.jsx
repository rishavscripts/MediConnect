import { useState } from 'react';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import { Search, Navigation, Phone } from 'lucide-react';

const SEED_HOSPITALS = [
  { id: 1, name: 'Panihati State General Hospital', spec: 'General Medicine', address: 'B.T. Road, Panihati', phone: '+91 33 2553 2211', lat: 22.6945, lng: 88.3748 },
  { id: 2, name: 'Apollo Clinic Barrackpore', spec: 'Cardiology', address: 'B.T. Road, Barrackpore', phone: '+91 33 2592 0100', lat: 22.7601, lng: 88.3702 },
  { id: 3, name: 'AMRI Hospital Salt Lake', spec: 'Neurology', address: 'JC-16 & 17, Sector III, Salt Lake', phone: '+91 33 6606 3800', lat: 22.5697, lng: 88.4122 },
  { id: 4, name: 'Techno India Dama Healthcare', spec: 'Orthopedics', address: 'EM Bypass, Kolkata', phone: '+91 33 4005 0000', lat: 22.5488, lng: 88.4015 },
];

export default function HospitalLocatorPage() {
  const [query, setQuery] = useState('');
  const [specialization, setSpecialization] = useState('All');
  const [center, setCenter] = useState([22.6945, 88.3748]);

  const filtered = SEED_HOSPITALS.filter((h) => {
    const matchesSpec = specialization === 'All' || h.spec === specialization;
    const matchesQuery = h.name.toLowerCase().includes(query.toLowerCase()) || h.address.toLowerCase().includes(query.toLowerCase());
    return matchesSpec && matchesQuery;
  });

  const handleGPS = () => {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition((pos) => {
        setCenter([pos.coords.latitude, pos.coords.longitude]);
      });
    }
  };

  return (
    <div className="space-y-5">
      <div className="flex flex-wrap items-center justify-between gap-4">
        <h1 className="text-2xl font-bold">Hospital & Doctor Locator</h1>
        <button
          onClick={handleGPS}
          className="inline-flex items-center gap-2 bg-teal-600 hover:bg-teal-700 text-white text-xs font-semibold px-4 py-2.5 rounded-xl"
        >
          <Navigation className="h-3.5 w-3.5" /> Use My GPS Location
        </button>
      </div>

      {/* Filter Bar */}
      <div className="grid sm:grid-cols-3 gap-3 bg-white dark:bg-slate-900 p-4 rounded-2xl border border-slate-200 dark:border-slate-800">
        <div className="sm:col-span-2 relative">
          <Search className="h-4 w-4 text-slate-400 absolute left-3.5 top-3" />
          <input
            type="text"
            placeholder="Search hospital name or locality..."
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            className="w-full pl-10 pr-4 py-2 rounded-xl border border-slate-200 dark:border-slate-700 bg-transparent text-sm"
          />
        </div>
        <select
          value={specialization}
          onChange={(e) => setSpecialization(e.target.value)}
          className="px-3.5 py-2 rounded-xl border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-900 text-sm"
        >
          <option value="All">All Specializations</option>
          <option value="General Medicine">General Medicine</option>
          <option value="Cardiology">Cardiology</option>
          <option value="Neurology">Neurology</option>
          <option value="Orthopedics">Orthopedics</option>
        </select>
      </div>

      {/* Split Results + Map */}
      <div className="grid lg:grid-cols-12 gap-6">
        <div className="lg:col-span-5 space-y-3 max-h-[520px] overflow-y-auto pr-1">
          {filtered.map((h) => (
            <div
              key={h.id}
              onClick={() => setCenter([h.lat, h.lng])}
              className="cursor-pointer bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 hover:border-teal-500 rounded-2xl p-4 transition"
            >
              <div className="flex justify-between items-start gap-2">
                <h3 className="font-bold text-base">{h.name}</h3>
                <span className="text-xs font-semibold px-2.5 py-0.5 rounded-full bg-teal-100 text-teal-800 dark:bg-teal-900/50 dark:text-teal-300">
                  {h.spec}
                </span>
              </div>
              <p className="text-xs text-slate-500 mt-1">{h.address}</p>
              <div className="mt-3 flex items-center justify-between">
                <span className="text-xs font-medium text-slate-600 dark:text-slate-300">{h.phone}</span>
                <a
                  href={`tel:${h.phone}`}
                  onClick={(e) => e.stopPropagation()}
                  className="inline-flex items-center gap-1.5 bg-emerald-600 hover:bg-emerald-700 text-white text-xs font-semibold px-3.5 py-1.5 rounded-lg"
                >
                  <Phone className="h-3.5 w-3.5" /> Call Now
                </a>
              </div>
            </div>
          ))}
        </div>

        <div className="lg:col-span-7 h-[520px] rounded-2xl overflow-hidden border border-slate-200 dark:border-slate-800">
          <MapContainer center={center} zoom={12} className="h-full w-full">
            <TileLayer
              attribution='&copy; OpenStreetMap contributors'
              url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
            />
            {filtered.map((h) => (
              <Marker key={h.id} position={[h.lat, h.lng]}>
                <Popup>
                  <strong>{h.name}</strong>
                  <br />
                  {h.spec} — {h.phone}
                </Popup>
              </Marker>
            ))}
          </MapContainer>
        </div>
      </div>
    </div>
  );
}