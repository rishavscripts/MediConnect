CREATE TABLE IF NOT EXISTS hospitals (
    id UUID PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    address TEXT NOT NULL,
    phone VARCHAR(40) NULL,
    latitude NUMERIC(10,7) NULL,
    longitude NUMERIC(10,7) NULL,
    source_name VARCHAR(120) NULL,
    source_reference TEXT NULL,
    verified_at TIMESTAMPTZ NULL
);

CREATE INDEX IF NOT EXISTS idx_hospitals_name ON hospitals(name);

CREATE TABLE IF NOT EXISTS doctors (
    id UUID PRIMARY KEY,
    hospital_id UUID NULL REFERENCES hospitals(id) ON DELETE SET NULL,
    name VARCHAR(160) NOT NULL,
    specialization VARCHAR(120) NOT NULL,
    contact_info VARCHAR(120) NULL,
    source_reference TEXT NULL,
    verified_at TIMESTAMPTZ NULL
);

CREATE INDEX IF NOT EXISTS idx_doctors_hospital_id ON doctors(hospital_id);
CREATE INDEX IF NOT EXISTS idx_doctors_specialization ON doctors(specialization);
CREATE INDEX IF NOT EXISTS idx_doctors_name ON doctors(name);
