# Technical Specification — Medi-Connect

**Status:** Initial architecture proposal  
**Architecture:** Modular monolith / monorepo  
**Frontend:** React, TypeScript, Vite  
**Backend:** Java, Spring Boot, Maven  
**Database:** PostgreSQL  
**API style:** REST, JSON, versioned under `/api/v1`

## 1. Architecture overview

```text
Browser
  |
  | HTTPS / JSON
  v
React + TypeScript (Vite)
  |
  | REST API
  v
Spring Boot Application
  |-- Authentication & Authorization
  |-- User/Profile
  |-- Medication Information
  |-- Hospital & Doctor Search
  |-- Medication Schedule / Appointment
  |-- Notifications
  |
  +---- PostgreSQL
  |
  +---- External providers (maps, medication reference, email/push)
```

Start with a modular monolith. Do not introduce microservices until there is a demonstrated operational or scaling requirement.

## 2. Repository layout

```text
Medi-Connect/
├── GEMINI.md
├── PRD.md
├── TechSpec.md
├── AppFlow.md
├── Design.md
├── Schema.md
├── Tracker.md
├── Rules.md
├── README.md
├── .gitignore
├── .env.example
├── docker-compose.yml
├── frontend/
├── backend/
├── database/
├── docs/
├── deployment/
└── scripts/
```

## 3. Frontend

### Suggested libraries
- React + TypeScript + Vite
- React Router for routing
- Axios or Fetch for HTTP
- React Hook Form + Zod for form handling/validation
- Tailwind CSS if selected as the styling system
- Lucide React for icons
- Vitest + React Testing Library for tests

Use the existing project configuration if already initialized. Avoid adding duplicate libraries that solve the same problem.

### Frontend organization

```text
frontend/src/
├── assets/
├── components/
│   ├── common/
│   ├── layout/
│   └── ui/
├── pages/
│   ├── public/
│   ├── auth/
│   ├── dashboard/
│   ├── medication/
│   ├── hospitals/
│   ├── assistant/
│   └── profile/
├── layouts/
├── routes/
├── services/
├── hooks/
├── context/
├── types/
├── utils/
├── constants/
├── styles/
├── App.tsx
└── main.tsx
```

Keep API access in service modules, not scattered across page components. Represent loading, empty, success, and error states explicitly.

## 4. Backend

Use Spring Boot with Spring Web, Spring Security, Spring Data JPA, Validation, PostgreSQL Driver, Flyway, and test dependencies. Add JWT support using a maintained library and verify its compatibility with the chosen Spring Security version.

### Package organization

```text
com.mediconnect/
├── MediConnectApplication.java
├── config/
├── security/
├── common/
│   ├── exception/
│   ├── response/
│   └── validation/
├── auth/
├── user/
├── medication/
├── hospital/
├── assistant/
└── notification/
```

Organize each business module internally with controller, service, repository, dto, and entity packages as needed.

### Layer responsibilities
- Controller: HTTP mapping, request validation, response status.
- Service: business rules and orchestration.
- Repository: persistence queries.
- Entity: persistence model.
- DTO: API request/response contracts.
- Configuration/security: application and security configuration.

Use constructor injection, DTOs, global exception handling, and transactions where required.

## 5. API conventions

Base path: `/api/v1`

Suggested endpoint groups:

| Area | Example endpoints |
|---|---|
| Auth | `POST /auth/register`, `POST /auth/login`, `POST /auth/refresh`, `POST /auth/logout` |
| Profile | `GET /users/me`, `PATCH /users/me` |
| Medical profile | `GET /users/me/medical-profile`, `PUT /users/me/medical-profile` |
| Hospitals | `GET /hospitals?location=&specialization=&page=` |
| Doctors | `GET /doctors?hospitalId=&specialization=` |
| Medication info | `GET /medications/search?q=` |
| User medication records | `GET /assistant/medications`, `POST /assistant/medications` |
| Schedules | `GET /assistant/schedules`, `POST /assistant/schedules`, `PATCH /assistant/schedules/{id}` |
| Dose events | `POST /assistant/dose-events/{id}/status` |
| Appointments | `GET /assistant/appointments`, `POST /assistant/appointments` |
| Notifications | `GET /notifications`, `PATCH /notifications/{id}/read` |

These are proposed contracts, not existing implemented endpoints. Confirm request/response shapes before coding.

### Response and errors
Use consistent JSON response contracts. Use standard HTTP status codes. Validation failures should return field-level details without stack traces. A common response envelope is optional; consistency matters more than the envelope itself.

## 6. Authentication and authorization

- Hash passwords using a strong adaptive password encoder such as BCrypt.
- Authenticate API requests using short-lived access tokens and a carefully designed refresh-token strategy.
- Do not store tokens in insecure browser storage without a threat assessment. Prefer secure, HttpOnly, SameSite cookies where the architecture supports them.
- Apply authorization at service/resource level, not only in the UI.
- Derive the current user identity from the authenticated principal; do not trust a client-supplied user ID for ownership.
- Protect all patient-specific records with ownership checks.
- Configure CORS with explicit allowed origins.
- Keep secrets outside source control.

## 7. Database and migrations

Use PostgreSQL and Flyway. Keep migration scripts in `backend/src/main/resources/db/migration/` or a clearly documented shared migration location.

Use UTC timestamps in persistence and convert to the user's timezone for display. Use `timestamptz` for instants and `date` for date-only concepts.

## 8. External integrations

### Maps / places
Create a provider abstraction so the application is not tightly coupled to one map provider. Keep secret keys on the server when the provider requires them. Validate and cache provider responses where appropriate.

### Medication reference
Use a reputable, documented source. Store source name, source URL/reference, and retrieval time for displayed information. Do not treat a drug-information API as a prescribing engine.

### Notifications
Start with in-app notifications. Add email or push later through a provider interface. Make notification processing idempotent and timezone-aware.

## 9. Configuration

Use environment variables for database credentials, JWT secrets, and external provider keys. Commit `.env.example` with placeholder values only. Never commit real `.env` files.

## 10. Testing

Backend: JUnit 5, Mockito, Spring Boot Test, MockMvc, and Testcontainers if appropriate.

Frontend: Vitest, React Testing Library, and API mocking.

Required tests include:
- Authentication and authorization.
- User data ownership.
- Request validation.
- Schedule creation and status transitions.
- Reminder timing and duplicate prevention.
- External provider failure handling.

## 11. Local development

Suggested local services:
- PostgreSQL via Docker Compose.
- Backend on a configurable local port (commonly 8080).
- Vite frontend on its configured development port (commonly 5173).

Document exact commands in README after project initialization. Do not assume ports or scripts without checking generated configuration.

## 12. Deployment direction

A first deployment can use a single backend service, managed PostgreSQL, and static frontend hosting or a reverse proxy. Use HTTPS, managed secrets, database backups, health checks, and structured logs. Add CI/CD after local builds and tests are stable.
