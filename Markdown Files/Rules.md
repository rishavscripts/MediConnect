# Project Rules — Medi-Connect

These rules apply to contributors and AI coding agents working in this repository.

## 1. General rules

1. Read `GEMINI.md`, `PRD.md`, `TechSpec.md`, `AppFlow.md`, `Design.md`, `Schema.md`, and the relevant tracker items before making substantial changes.
2. Inspect the existing code before creating or modifying files.
3. Follow the established architecture and naming conventions.
4. Prefer small, reviewable changes over broad rewrites.
5. Do not invent existing endpoints, entities, files, or integrations.
6. Ask for clarification when a requirement materially affects architecture, privacy, or safety.
7. Keep documentation and `Tracker.md` synchronized with implemented behavior.
8. Do not claim tests passed unless they were actually run.

## 2. Frontend rules

- Use the existing React + TypeScript conventions.
- Prefer functional components and typed props.
- Keep components focused and reusable.
- Keep API calls in service modules.
- Use centralized route definitions and protect authenticated routes.
- Handle loading, empty, validation, success, and error states.
- Use semantic HTML and accessible labels.
- Avoid putting secrets in frontend environment variables; frontend build variables are public.
- Do not trust frontend-only authorization checks.

## 3. Backend rules

- Use layered/module-oriented architecture.
- Keep controllers thin.
- Put business logic in services.
- Use repositories for persistence.
- Use DTOs for API input/output.
- Use constructor injection.
- Validate request DTOs.
- Use centralized exception handling.
- Use transactions where appropriate.
- Avoid returning JPA entities directly from public APIs.
- Use pagination for potentially large result sets.
- Keep API contracts consistent and documented.

## 4. Security rules

- Never commit secrets, API keys, passwords, private certificates, or real `.env` files.
- Never store plaintext passwords.
- Never log passwords, tokens, or sensitive medical details.
- Derive user identity from the authenticated server-side principal.
- Never trust a user ID supplied by the browser to authorize access.
- Verify ownership for every patient-owned resource.
- Apply least privilege to roles and service accounts.
- Configure CORS with explicit origins.
- Use HTTPS in deployed environments.
- Return safe error messages; do not expose stack traces.
- Review dependencies and keep them reasonably current.

## 5. Healthcare safety and privacy rules

- Medi-Connect is an assistance and information platform, not a diagnostic or prescribing service.
- Do not implement autonomous diagnosis or personalized medication/dosage recommendations.
- Do not infer a medicine or dose from symptoms.
- Medication schedules must be based on instructions entered from an existing prescription or qualified clinician.
- Do not silently modify medication instructions.
- Display source attribution for external medication information.
- Distinguish user-entered information from externally sourced/verified information.
- Make emergency guidance clear; do not imply that the application replaces emergency services.
- Treat medical history, allergies, and medication records as sensitive.
- Collect the minimum data needed for a feature.
- Restrict health information to the user and explicitly authorized workflows.
- Do not use real patient data in development, screenshots, seed data, or tests.

## 6. Database rules

- Use Flyway migrations for schema changes.
- Do not modify an already-applied migration in shared environments; create a new migration.
- Add foreign keys and appropriate indexes.
- Use UTC instants and explicit timezones for schedules.
- Enforce valid status values and date ranges.
- Avoid storing redundant or unnecessary personal data.
- Define deletion and retention behavior before production.
- Use synthetic data for development and testing.

## 7. API rules

- Use `/api/v1` as the API version prefix.
- Use HTTP methods according to REST semantics.
- Use appropriate status codes.
- Validate all external input.
- Do not expose internal implementation details.
- Document request/response changes.
- Make reminder creation and external notification delivery idempotent where possible.
- Handle third-party provider timeouts and failures gracefully.

## 8. Testing rules

- Add tests for new business logic.
- Test both successful and failure paths.
- Test authorization and cross-user data isolation.
- Test validation boundaries.
- Test date/time and timezone behavior for schedules.
- Mock external services in unit tests.
- Use integration tests for important database/API workflows.
- Never use real credentials or real patient information in tests.

## 9. Antigravity / Gemini agent workflow

Before implementing a task:
1. Read the relevant project documentation.
2. Inspect the existing files and dependencies.
3. Summarize the proposed changes.
4. Identify files likely to be created or modified.
5. Identify security, privacy, and data migration implications.

During implementation:
1. Make only changes relevant to the task.
2. Preserve existing working functionality.
3. Follow project naming and formatting conventions.
4. Avoid unnecessary dependency additions.
5. Do not replace real implementations with mock data unless explicitly requested.

After implementation:
1. Run relevant build and test commands.
2. Report commands actually run and their results.
3. List created and modified files.
4. Explain configuration or migration steps.
5. Update `Tracker.md` and documentation when appropriate.
6. Clearly disclose anything not tested or still incomplete.

## 10. Conflict resolution

If project documents conflict:
1. Follow explicit user instructions for the current task unless they create a security or safety issue.
2. Follow the most recently confirmed architecture decision.
3. Prefer `PRD.md` for product scope, `TechSpec.md` for technical architecture, `Schema.md` for data modeling, and `Design.md` for UI conventions.
4. Record unresolved conflicts in `Tracker.md`.
5. Do not silently make a consequential product or security decision.

## 11. Quality standard

Code should be understandable, secure, testable, maintainable, and proportionate to the current project stage. Avoid premature microservices, unnecessary abstractions, and unverified medical logic.
