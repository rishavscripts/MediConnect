# Application Flow — Medi-Connect

## 1. Actors

- **Visitor:** unauthenticated person viewing public pages.
- **Patient:** authenticated user managing their own information and assistance workflows.
- **Administrator:** authorized operator managing platform-level data, if this role is enabled.

Do not assume administrators can view all sensitive medical data by default. Define least-privilege permissions explicitly.

## 2. High-level navigation

```text
Public Landing
   |-- About
   |-- Contact
   |-- Sign Up
   `-- Login
          |
          v
       Dashboard
          |-- Profile & Medical History
          |-- Medication Assistance
          |-- Hospital & Doctor Search
          `-- Personal Medication Assistant
                 |-- Medication List
                 |-- Schedule Calendar
                 |-- Dose History
                 |-- Appointments
                 `-- Notifications
```

## 3. Registration and login flow

1. Visitor opens the landing page.
2. Visitor selects Sign Up or Login.
3. Registration form collects only required account information.
4. Backend validates the request and checks account uniqueness.
5. Password is securely hashed before persistence.
6. Login validates credentials and establishes an authenticated session/token.
7. Frontend navigates to the dashboard.
8. Protected routes redirect unauthenticated users to login.

Failure states:
- Invalid fields.
- Existing account.
- Incorrect credentials.
- Expired session.
- Backend unavailable.

## 4. Profile flow

1. Patient opens Profile.
2. Frontend requests the authenticated user's profile.
3. Patient reviews or edits permitted fields.
4. Backend validates and persists changes.
5. Frontend displays a success or error state.

Medical history and allergies are sensitive. Show them only to the authenticated owner and authorized workflows.

## 5. Hospital and doctor search flow

1. Patient opens Hospital Search.
2. Patient enters a location or chooses optional browser location permission.
3. Patient selects a specialization or search term.
4. Frontend sends filters to the backend.
5. Backend validates filters and queries its own records and/or an approved external provider.
6. Results are normalized and returned.
7. Frontend displays result cards and map links where available.
8. Patient opens a result to view details or use the verified contact information.

States to support:
- Initial search.
- Loading.
- Results found.
- No results.
- Location permission denied.
- Provider unavailable.
- Invalid search input.

For urgent symptoms, do not make users rely on search ranking; prominently advise contacting local emergency services or seeking immediate professional care.

## 6. Medication information flow

1. Patient opens Medication Assistance.
2. Patient enters a question or symptom information.
3. Frontend explains that this feature provides general information, not diagnosis.
4. Backend validates input and retrieves information from approved sources or a reviewed ruleset.
5. Results include source attribution, limitations, and appropriate professional-care guidance.
6. Patient can return to the result or seek professional advice.

Do not output autonomous prescriptions or personalized dosage recommendations. Do not infer a medication from symptoms. Allergy flags must be treated as safety-critical and should not be hidden.

## 7. Personal medication assistant flow

1. Patient opens Medication Assistant.
2. Patient adds a medication based on an existing prescription or clinician instruction.
3. Patient enters the schedule exactly as instructed.
4. Backend validates dates/times and persists the medication and schedule.
5. Reminder service creates notification events.
6. Patient receives an in-app reminder (additional channels may be added later).
7. Patient marks the scheduled dose as taken or skipped.
8. Backend records the event and updates the history.
9. Patient reviews upcoming schedules and history.

Schedule changes should preserve an audit/history record where practical. Do not silently change dosage or timing.

## 8. Appointment flow

1. Patient opens Appointments.
2. Patient adds a check-up appointment with date/time and optional provider details.
3. Backend validates and stores the appointment.
4. Reminder events are generated.
5. Patient can view, update, or cancel their own appointment.

## 9. Notification flow

```text
Schedule / Appointment saved
          |
          v
Notification event created
          |
          v
Due-time worker checks pending event
          |
          v
Delivery provider (in-app first)
          |
          v
Delivery status recorded
```

Use idempotency keys or unique constraints to avoid duplicate delivery. Handle timezone changes and daylight-saving rules where applicable.

## 10. Route access matrix

| Route / area | Visitor | Patient | Admin |
|---|---:|---:|---:|
| Landing/About/Contact | Yes | Yes | Yes |
| Register/Login | Yes | Optional | Optional |
| Dashboard | No | Own account | Admin-only view |
| Own profile | No | Own data | Only if explicitly authorized |
| Hospital search | Optional/public subset | Yes | Manage catalog if authorized |
| Medication assistant | No | Own records | No default access |
| Notifications | No | Own notifications | No default access |

## 11. Global UX states

Every data-driven screen should account for:
- Loading.
- Empty state.
- Success.
- Validation error.
- Authorization failure.
- Network/server failure.
- Partial external-provider failure.

## 12. Flow acceptance criteria

- Unauthenticated users cannot access patient-specific pages or APIs.
- A patient cannot read or modify another patient's records.
- Hospital search handles denied location permission.
- Medication information displays source and safety context.
- Medication schedules are based on user-entered clinician instructions.
- Dose status changes are persisted and visible in history.
- Duplicate reminders are prevented.
