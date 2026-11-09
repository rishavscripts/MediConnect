# Medi-Connect — Development Tracker

**Purpose:** Track implementation progress and decisions.  
**Status:** Project planning / pre-implementation.

## 1. Status legend

- `NOT STARTED` — not yet begun.
- `IN PROGRESS` — actively being implemented.
- `BLOCKED` — waiting on a decision or dependency.
- `IN REVIEW` — implemented, awaiting review/testing.
- `DONE` — acceptance criteria met and verified.

## 2. Milestones

| ID | Milestone | Status | Notes |
|---|---|---|---|
| M-01 | Repository and workspace setup | NOT STARTED | Monorepo |
| M-02 | React + TypeScript initialization | NOT STARTED | Vite |
| M-03 | Spring Boot initialization | NOT STARTED | Maven |
| M-04 | PostgreSQL and Flyway setup | NOT STARTED | Local development |
| M-05 | API health check and CORS | NOT STARTED | Frontend/backend connection |
| M-06 | Authentication and authorization | NOT STARTED | JWT/session strategy |
| M-07 | Patient profile | NOT STARTED | Include medical profile carefully |
| M-08 | Dashboard and navigation | NOT STARTED | Responsive |
| M-09 | Hospital and doctor search | NOT STARTED | Provider decision needed |
| M-10 | Medication information | NOT STARTED | Reliable source and safety review |
| M-11 | Medication assistant | NOT STARTED | User-entered clinician instructions |
| M-12 | Notifications | NOT STARTED | In-app first |
| M-13 | Automated tests | NOT STARTED | Frontend and backend |
| M-14 | Deployment and documentation | NOT STARTED | HTTPS, backups, secrets |

## 3. Feature backlog

### Foundation
- [ ] Create root repository structure.
- [ ] Add `GEMINI.md` and project documentation.
- [ ] Configure `.gitignore`.
- [ ] Add safe `.env.example` files.
- [ ] Initialize frontend.
- [ ] Initialize backend.
- [ ] Configure PostgreSQL.
- [ ] Configure Flyway.
- [ ] Add backend health endpoint.
- [ ] Verify frontend-to-backend communication.

### Authentication and profile
- [ ] Registration.
- [ ] Login and logout.
- [ ] Password hashing.
- [ ] Token/session lifecycle.
- [ ] Protected frontend routes.
- [ ] Backend authorization.
- [ ] Profile read/update.
- [ ] Medical history and allergy management.
- [ ] Ownership and authorization tests.

### Hospital and doctor discovery
- [ ] Decide hospital data source.
- [ ] Hospital search API.
- [ ] Doctor search API.
- [ ] Specialization filters.
- [ ] Location input.
- [ ] Optional location permission.
- [ ] Results and detail UI.
- [ ] Provider error and empty states.

### Medication information
- [ ] Select reputable reference source.
- [ ] Document source attribution.
- [ ] Build information search.
- [ ] Display limitations and professional-care guidance.
- [ ] Review safety boundaries.
- [ ] Test allergy-related display behavior.

### Personal medication assistant
- [ ] Add user medication record.
- [ ] Add schedule based on supplied instructions.
- [ ] Calendar/list view.
- [ ] Generate due events.
- [ ] In-app reminders.
- [ ] Taken/skipped status.
- [ ] Dose history.
- [ ] Appointment CRUD.
- [ ] Duplicate reminder prevention.
- [ ] Timezone tests.

### Release readiness
- [ ] Frontend tests.
- [ ] Backend unit tests.
- [ ] API integration tests.
- [ ] Authorization tests.
- [ ] Accessibility review.
- [ ] Security review.
- [ ] Database backup/restore test.
- [ ] Production configuration.
- [ ] Deployment verification.
- [ ] README and API documentation.

## 4. Definition of Done

A task is complete only when:
1. Acceptance criteria are met.
2. Code follows `Rules.md` and `TechSpec.md`.
3. Validation and error states are handled.
4. Relevant tests pass.
5. No secrets are committed.
6. Authorization and data ownership are checked where applicable.
7. Documentation is updated when behavior or setup changes.
8. The change has been reviewed.

## 5. Decision log

| Date | Decision | Reason | Status |
|---|---|---|---|
| TBD | Use monorepo | Keep frontend/backend/docs together | Proposed |
| TBD | PostgreSQL baseline | Relational data and migration support | Proposed |
| TBD | Modular monolith | Simpler initial deployment and development | Proposed |
| TBD | In-app notifications first | Reduce initial integration complexity | Proposed |

## 6. Blockers and open questions

- [ ] Confirm PostgreSQL or MySQL.
- [ ] Confirm frontend styling system.
- [ ] Confirm Java and Spring Boot versions.
- [ ] Select hospital/place data provider.
- [ ] Select medication reference provider.
- [ ] Decide authentication token storage and refresh strategy.
- [ ] Decide notification provider and channels.
- [ ] Review applicable privacy and health-data obligations.
- [ ] Define account and medical-data retention/deletion policy.

Update this tracker whenever a milestone, task, or decision changes. Do not mark work `DONE` without verification.
