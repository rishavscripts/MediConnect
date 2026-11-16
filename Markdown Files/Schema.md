# Database Schema — Medi-Connect

**Database:** PostgreSQL (recommended)  
**Migration tool:** Flyway  
**Status:** Proposed logical schema; refine before implementation.

## 1. Modeling principles

- Use UUID primary keys for externally referenced records.
- Use `timestamptz` for timestamps representing instants; store in UTC.
- Use `date` for date-only values.
- Use foreign keys and indexes for integrity and query performance.
- Keep patient-owned data linked to a user.
- Store only data necessary for the product.
- Never store plaintext passwords, access tokens, or provider secrets.
- Restrict access to medical and personal records by ownership and authorization.

## 2. Core entities

### users
| Column | Type | Constraints |
|---|---|---|
| id | UUID | PK |
| full_name | VARCHAR(120) | NOT NULL |
| email | VARCHAR(255) | UNIQUE, NOT NULL |
| password_hash | VARCHAR(255) | NOT NULL |
| role | VARCHAR(30) | NOT NULL, default `PATIENT` |
| is_active | BOOLEAN | NOT NULL, default true |
| created_at | TIMESTAMPTZ | NOT NULL |
| updated_at | TIMESTAMPTZ | NOT NULL |

### user_profiles
| Column | Type | Constraints |
|---|---|---|
| id | UUID | PK |
| user_id | UUID | FK users.id, UNIQUE |
| date_of_birth | DATE | NULL |
| phone | VARCHAR(30) | NULL |
| address | TEXT | NULL |
| created_at | TIMESTAMPTZ | NOT NULL |
| updated_at | TIMESTAMPTZ | NOT NULL |

Collect only profile fields that have a clear product purpose. Avoid collecting age and date of birth redundantly.

### medical_conditions
| Column | Type | Constraints |
|---|---|---|
| id | UUID | PK |
| user_id | UUID | FK users.id |
| condition_name | VARCHAR(160) | NOT NULL |
| notes | TEXT | NULL |
| recorded_at | TIMESTAMPTZ | NOT NULL |

### allergies
| Column | Type | Constraints |
|---|---|---|
| id | UUID | PK |
| user_id | UUID | FK users.id |
| substance | VARCHAR(160) | NOT NULL |
| reaction_notes | TEXT | NULL |
| recorded_at | TIMESTAMPTZ | NOT NULL |

### hospitals
| Column | Type | Constraints |
|---|---|---|
| id | UUID | PK |
| name | VARCHAR(200) | NOT NULL |
| address | TEXT | NOT NULL |
| phone | VARCHAR(40) | NULL |
| latitude | NUMERIC(10,7) | NULL |
| longitude | NUMERIC(10,7) | NULL |
| source_name | VARCHAR(120) | NULL |
| source_reference | TEXT | NULL |
| verified_at | TIMESTAMPTZ | NULL |

### doctors
| Column | Type | Constraints |
|---|---|---|
| id | UUID | PK |
| hospital_id | UUID | FK hospitals.id, NULL if independent |
| name | VARCHAR(160) | NOT NULL |
| specialization | VARCHAR(120) | NOT NULL |
| contact_info | VARCHAR(120) | NULL |
| source_reference | TEXT | NULL |
| verified_at | TIMESTAMPTZ | NULL |

### medication_catalog
Reference information only; not a prescribing table.

| Column | Type | Constraints |
|---|---|---|
| id | UUID | PK |
| name | VARCHAR(200) | NOT NULL |
| generic_name | VARCHAR(200) | NULL |
| description | TEXT | NULL |
| source_name | VARCHAR(120) | NOT NULL |
| source_url | TEXT | NULL |
| retrieved_at | TIMESTAMPTZ | NOT NULL |

### user_medications
A user's record of a medication based on an existing prescription or clinician instruction.

| Column | Type | Constraints |
|---|---|---|
| id | UUID | PK |
| user_id | UUID | FK users.id |
| catalog_id | UUID | FK medication_catalog.id, NULL |
| display_name | VARCHAR(200) | NOT NULL |
| instruction_text | TEXT | NOT NULL |
| start_date | DATE | NULL |
| end_date | DATE | NULL |
| prescribing_context | VARCHAR(200) | NULL |
| is_active | BOOLEAN | NOT NULL, default true |
| created_at | TIMESTAMPTZ | NOT NULL |
| updated_at | TIMESTAMPTZ | NOT NULL |

`instruction_text` records the user's supplied instructions. The application must not calculate or prescribe a dose.

### medication_schedules
| Column | Type | Constraints |
|---|---|---|
| id | UUID | PK |
| user_medication_id | UUID | FK user_medications.id |
| time_of_day | TIME | NOT NULL |
| timezone | VARCHAR(80) | NOT NULL |
| start_date | DATE | NOT NULL |
| end_date | DATE | NULL |
| recurrence_rule | VARCHAR(255) | NULL |
| is_active | BOOLEAN | NOT NULL, default true |
| created_at | TIMESTAMPTZ | NOT NULL |

### dose_events
| Column | Type | Constraints |
|---|---|---|
| id | UUID | PK |
| schedule_id | UUID | FK medication_schedules.id |
| scheduled_for | TIMESTAMPTZ | NOT NULL |
| status | VARCHAR(20) | NOT NULL |
| recorded_at | TIMESTAMPTZ | NULL |
| notes | TEXT | NULL |

Suggested status values: `PENDING`, `TAKEN`, `SKIPPED`, `MISSED`. Define the allowed state transitions in backend logic.

### appointments
| Column | Type | Constraints |
|---|---|---|
| id | UUID | PK |
| user_id | UUID | FK users.id |
| hospital_id | UUID | FK hospitals.id, NULL |
| doctor_id | UUID | FK doctors.id, NULL |
| title | VARCHAR(160) | NOT NULL |
| appointment_at | TIMESTAMPTZ | NOT NULL |
| timezone | VARCHAR(80) | NOT NULL |
| notes | TEXT | NULL |
| status | VARCHAR(20) | NOT NULL |
| created_at | TIMESTAMPTZ | NOT NULL |

### notifications
| Column | Type | Constraints |
|---|---|---|
| id | UUID | PK |
| user_id | UUID | FK users.id |
| type | VARCHAR(30) | NOT NULL |
| title | VARCHAR(160) | NOT NULL |
| message | TEXT | NOT NULL |
| scheduled_for | TIMESTAMPTZ | NULL |
| sent_at | TIMESTAMPTZ | NULL |
| read_at | TIMESTAMPTZ | NULL |
| status | VARCHAR(20) | NOT NULL |
| idempotency_key | VARCHAR(180) | UNIQUE |
| created_at | TIMESTAMPTZ | NOT NULL |

## 3. Relationship overview

```text
users
 ├── 1:1 user_profiles
 ├── 1:N medical_conditions
 ├── 1:N allergies
 ├── 1:N user_medications
 │       └── N:1 medication_catalog (optional)
 │       └── 1:N medication_schedules
 │                 └── 1:N dose_events
 ├── 1:N appointments
 └── 1:N notifications

hospitals
 └── 1:N doctors

appointments
 ├── N:1 hospitals (optional)
 └── N:1 doctors (optional)
```

## 4. Important indexes and constraints

Recommended:
- Unique index on normalized user email.
- Index `user_id` on all patient-owned tables.
- Index `appointment_at` for appointment queries.
- Index `scheduled_for` and `status` for due notification/dose processing.
- Index hospital location columns if geospatial search is implemented; consider PostGIS only if needed.
- Unique idempotency key for notification creation.
- Check constraints for status values and valid date ranges.

## 5. Data lifecycle and privacy

- Define retention and deletion policies before production.
- Avoid logging health data.
- Restrict database access by environment and least privilege.
- Back up the database and test restoration.
- Decide how account deletion affects dependent records.
- Do not expose internal database IDs or records belonging to other users without authorization.

## 6. Migration plan

Use sequential Flyway migrations, for example:

```text
V1__create_users_and_profiles.sql
V2__create_medical_conditions_and_allergies.sql
V3__create_hospitals_and_doctors.sql
V4__create_medication_catalog_and_user_medications.sql
V5__create_schedules_and_dose_events.sql
V6__create_appointments_and_notifications.sql
```

This is a proposed schema, not a migration script. Review entity relationships, privacy needs, and query patterns before writing migrations.
