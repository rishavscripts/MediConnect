# Product Requirements Document (PRD) — Medi-Connect

**Project:** Medi-Connect  
**Product type:** Full-stack healthcare assistance web application  
**Status:** Initial product definition  
**Frontend:** React + TypeScript + Vite  
**Backend:** Java + Spring Boot  
**Database:** PostgreSQL (recommended baseline)

## 1. Product vision

Medi-Connect is a web-based healthcare assistance platform intended to help users access healthcare information, discover hospitals and doctors, and organize medication schedules and check-up reminders in one place.

It is an assistance and information platform. It is not a substitute for a qualified healthcare professional, emergency service, diagnosis, or prescription.

## 2. Problem statement

Patients may need to use separate sources to find healthcare facilities, understand general medication information, and keep track of prescribed medicines and appointments. Medi-Connect aims to bring these assistance workflows together in a single, accessible application.

## 3. Product goals

1. Provide a clear, accessible entry point to the platform.
2. Allow users to register, authenticate, and manage their profiles.
3. Provide reliable, safety-conscious medication information.
4. Help users discover hospitals and doctors by location and specialization.
5. Help users organize clinician-prescribed medication schedules and check-up appointments.
6. Protect personal and health-related information.

## 4. Target users

- Patients or caregivers who want to organize healthcare information.
- Users looking for hospitals or doctors by location and specialization.
- Users who want reminders for an existing prescribed medication schedule or appointment.

Detailed personas and accessibility requirements should be refined during user research.

## 5. Scope

### 5.1 Core modules

#### A. Basic Medication Assistance
- Collect user-entered symptom information.
- Reference relevant profile information, such as recorded allergies and medical history, where authorized.
- Present general, sourced medication information and health guidance.
- Present diet or lifestyle information only when supported by reliable sources.
- Clearly communicate uncertainty and when professional care is needed.

**Safety boundary:** The initial product must not autonomously diagnose conditions, prescribe medicines, or generate personalized dosage instructions. Medication schedules must be entered from instructions supplied by a qualified healthcare professional or an existing prescription.

#### B. Hospital & Doctor Recommendation
- Search hospitals and doctors.
- Filter by specialization.
- Accept a manually entered location; location permission may be optional.
- Display available name, specialization, address, contact details, and map information where verified.
- Provide a route or map link where supported.
- Make emergency guidance prominent and advise users to contact local emergency services for urgent situations.

#### C. Personal Medication Assistant
- Add a medication record based on an existing prescription or clinician instruction.
- Create daily or weekly schedules.
- Record reminder times.
- Display upcoming check-up appointments.
- Allow users to mark a scheduled dose as taken or skipped.
- Show schedule history and adherence summaries without making clinical claims.

### 5.2 Supporting capabilities
- Public landing, About, and Contact pages.
- Sign-up, login, logout, and password recovery.
- Patient profile management.
- Dashboard with summaries of the three core modules.
- Notifications (in-app first; email/push integrations can follow).
- Responsive layout and accessible controls.

### 5.3 Out of scope for the initial release
- Autonomous diagnosis or treatment decisions.
- Automated prescribing or dosage calculation.
- Telemedicine consultations.
- Wearable-device synchronization.
- Insurance claims or payments.
- Clinical records interoperability.
- AI chatbot making medical decisions.

These may be considered later only after safety, legal, clinical, and technical review.

## 6. Functional requirements

| ID | Requirement | Priority |
|---|---|---|
| FR-01 | A visitor can view public product information. | Must |
| FR-02 | A user can register and log in securely. | Must |
| FR-03 | An authenticated user can view and update their profile. | Must |
| FR-04 | A user can record relevant medical history and allergies. | Must |
| FR-05 | A user can search hospitals by location and specialization. | Must |
| FR-06 | A user can view hospital and doctor details. | Must |
| FR-07 | A user can store medication information from an existing prescription. | Must |
| FR-08 | A user can create and manage reminders for that schedule. | Must |
| FR-09 | A user can mark a scheduled dose as taken or skipped. | Must |
| FR-10 | A user can create and view check-up appointments. | Should |
| FR-11 | A user can view medication information with source attribution. | Should |
| FR-12 | A user can manage notification preferences. | Should |
| FR-13 | The application provides an accessible responsive interface. | Must |

## 7. Non-functional requirements

- **Security:** secure password hashing, authenticated endpoints, authorization checks, secret management, and safe error responses.
- **Privacy:** collect only necessary data; restrict access to the owning user; avoid sensitive data in logs.
- **Reliability:** handle database and third-party API failures gracefully.
- **Maintainability:** modular frontend and backend, clear naming, tests, and documentation.
- **Performance:** paginate potentially large result sets and avoid unnecessary database queries.
- **Accessibility:** semantic HTML, keyboard navigation, visible focus, labels, and sufficient contrast.
- **Auditability:** record important changes to medication schedules and user-owned records where appropriate.

## 8. User journeys

1. Visitor → Landing page → Register/Login → Dashboard.
2. User → Hospital search → Apply location/specialization filters → View results/details.
3. User → Medication assistant → Add prescribed medication → Set schedule → Receive reminder → Update dose status.
4. User → Profile → Review/update contact information, medical history, and allergies.

## 9. Success measures

Initial measurable indicators:
- Registration and login flows complete successfully.
- A user can only access their own protected records.
- Hospital search returns appropriately filtered results when a data provider is configured.
- Medication schedules and dose-status updates persist correctly.
- Reminder events are generated according to configured schedules.
- Critical user journeys pass automated tests.

Do not claim clinical effectiveness based only on software usage metrics.

## 10. Assumptions and open decisions

- PostgreSQL is the recommended default; MySQL remains an alternative.
- Google Maps or another location provider requires separate API setup and billing review.
- Medication data provider is not yet selected.
- Notification channel and provider are not yet finalized.
- Applicable privacy, health-data, and consumer-protection obligations must be reviewed before public deployment.
- The product owner must decide whether user roles beyond `PATIENT` and `ADMIN` are needed.
