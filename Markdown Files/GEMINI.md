# GEMINI.md — Medi-Connect

> **Project:** Medi-Connect  
> **Project Type:** Full-Stack Healthcare Assistance Web Application  
> **Frontend:** React.js  
> **Backend:** Java Spring Boot  
> **Database:** PostgreSQL / MySQL  
> **Purpose:** Personal Full-Stack Development Project

---

# 1. Project Overview

Medi-Connect is a full-stack healthcare assistance web application designed to make healthcare information and essential patient services more accessible.

The application provides a centralized platform where users can access medication information, discover hospitals and doctors, and manage their prescribed medication schedules.

The primary objective is to develop a secure, scalable, maintainable, and user-friendly healthcare platform.

### Core Modules

1. Basic Medication Assistance
2. Hospital & Doctor Recommendation
3. Personal Medication Assistant

### Development Philosophy

- Follow clean architecture and separation of concerns.
- Build modular and reusable components.
- Maintain secure communication between frontend and backend.
- Follow REST API development standards.
- Prioritize patient data privacy and application security.
- Write maintainable, readable, and well-documented code.
- Avoid unnecessary complexity and overengineering.

---

# 2. Technology Stack

Use the following technologies as the project's preferred technology stack.

| Layer | Technology |
|---|---|
| Frontend | React.js |
| Language | JavaScript / TypeScript (follow existing project) |
| Styling | Follow existing styling framework |
| Backend | Java Spring Boot |
| API Architecture | RESTful APIs |
| ORM | Spring Data JPA / Hibernate |
| Database | PostgreSQL / MySQL |
| Authentication | Spring Security + JWT |
| API Communication | Axios / Fetch API |
| Maps Integration | Google Maps API |
| Notifications | Firebase / Email integration |
| Build Tool | Maven / Gradle (follow existing project) |
| Version Control | Git & GitHub |

**Important:** Do not introduce new technologies or dependencies without checking the existing project configuration and explaining why the addition is necessary.

---

# 3. Application Modules

## 3.1 Basic Medication Assistance

This module provides users with healthcare information based on their reported symptoms and available medical history.

### Expected Features

- Symptom information input.
- Medical history integration.
- Allergy information checking.
- Medication information retrieval.
- Diet and lifestyle guidance.
- Medication information display.

### Functional Workflow

1. User enters their symptoms.
2. System retrieves relevant patient profile information.
3. System checks recorded allergies and medical history.
4. System provides relevant, reliable healthcare information.
5. Results are displayed in an understandable format.

### Development Guidelines

- Keep symptom-processing logic separate from controllers.
- Use service classes for business logic.
- Validate all incoming requests.
- Avoid hardcoded medication information.
- Use reliable medication information sources.
- Never present an unvalidated algorithm as a medical diagnostic system.

**Medical Safety:** Medi-Connect is an assistance and information platform, not a replacement for a qualified healthcare professional. Do not autonomously prescribe medication or generate personalized dosages. Medication schedules must be based on prescriptions or instructions provided by qualified healthcare professionals.

---

## 3.2 Hospital & Doctor Recommendation

This module helps users discover hospitals and doctors based on their location and healthcare requirements.

### Expected Features

- Hospital search.
- Doctor search.
- Medical specialization filters.
- Location-based hospital discovery.
- Hospital contact information.
- Google Maps integration.
- Emergency hospital discovery.

### Functional Workflow

1. User selects a medical specialization or enters a healthcare requirement.
2. User provides a location or grants location permission.
3. Backend processes the search request.
4. Relevant hospital and doctor information is retrieved.
5. Frontend displays the results with location and contact information.

### Development Guidelines

- Keep hospital and doctor entities separate.
- Use external APIs through backend services where appropriate.
- Never expose private API keys in frontend code.
- Implement pagination where necessary.
- Validate external API responses.
- Handle unavailable external services gracefully.

---

## 3.3 Personal Medication Assistant

This module allows users to manage medication schedules and medical appointments.

### Expected Features

- Add prescribed medications.
- Create medication schedules.
- Daily and weekly calendar views.
- Medication reminders.
- Doctor appointment scheduling.
- Upcoming appointment notifications.
- Completed and missed dose tracking.
- Medication history.

### Functional Workflow

1. User adds a prescribed medication.
2. User configures the prescribed schedule.
3. System stores the schedule.
4. Notifications are generated according to the configured schedule.
5. User marks scheduled doses as completed or missed.
6. System updates the medication history.

### Development Guidelines

- Every schedule must belong to an authenticated user.
- Store timestamps consistently.
- Handle timezone differences.
- Avoid duplicate notifications.
- Track medication schedule changes.
- Do not independently modify prescribed dosages.

---

# 4. Frontend Development Guidelines

The React application must follow a modular component-based architecture.

### Architecture

Organize the frontend into logical directories:

- `components/` — Reusable UI components.
- `pages/` — Application pages.
- `layouts/` — Shared page layouts.
- `services/` — API communication.
- `hooks/` — Custom React hooks.
- `context/` — Global application state.
- `utils/` — Utility functions.
- `assets/` — Images, icons, and static assets.

Follow the existing repository structure if it is already established.

### Frontend Principles

1. Use functional React components.
2. Use React Hooks appropriately.
3. Avoid unnecessary component duplication.
4. Keep API logic outside presentation components.
5. Use reusable form components.
6. Implement proper loading and error states.
7. Use protected routes for authenticated pages.
8. Maintain responsive layouts.
9. Ensure accessibility.
10. Avoid unnecessary global state.

### UI/UX Guidelines

Medi-Connect should have a professional healthcare-oriented interface.

Preferred design direction:

- Clean and modern interface.
- Healthcare-inspired blue and white color palette.
- Consistent typography.
- Clear navigation.
- Well-organized dashboard.
- Reusable cards and forms.
- Responsive desktop, tablet, and mobile layouts.
- Accessible color contrast.
- Meaningful loading indicators and notifications.

Avoid cluttered interfaces, excessive animations, and inconsistent styling.

---

# 5. Backend Development Guidelines

The Spring Boot backend must follow a layered architecture.

### Recommended Layers

**Controller Layer**
- Handles HTTP requests.
- Validates incoming request data.
- Returns appropriate HTTP responses.
- Delegates business logic to services.

**Service Layer**
- Contains application business logic.
- Coordinates repository operations.
- Handles integrations with external services.

**Repository Layer**
- Handles database operations.
- Uses Spring Data JPA.
- Avoids placing business logic inside repositories.

**Entity Layer**
- Represents database entities.
- Defines relationships and constraints.

**DTO Layer**
- Defines request and response objects.
- Prevents direct exposure of sensitive entity fields.

**Configuration Layer**
- Contains security, JWT, CORS, and application configuration.

### Backend Coding Rules

1. Follow SOLID principles.
2. Use constructor-based dependency injection.
3. Prefer DTOs over exposing JPA entities.
4. Use meaningful class and method names.
5. Implement centralized exception handling.
6. Validate requests using Jakarta Bean Validation.
7. Use transactions where necessary.
8. Avoid unnecessary database queries.
9. Implement pagination for large datasets.
10. Follow REST API conventions.

### API Response Standards

Maintain consistent response structures across endpoints.

Example:

```json
{
  "success": true,
  "message": "Operation completed successfully",
  "data": {}
}
```

Use appropriate HTTP status codes.

---

# 6. Database Development Guidelines

Use PostgreSQL or MySQL according to the existing project configuration.

### Core Database Entities

| Entity | Purpose |
|---|---|
| User | Patient account information |
| UserProfile | Extended patient profile |
| Medication | Medication information |
| Hospital | Hospital information |
| Doctor | Doctor information |
| Schedule | Prescribed medication schedules |
| Notification | User notification records |
| Appointment | Doctor appointment information |

### Database Principles

- Use meaningful table and column names.
- Define primary and foreign keys.
- Maintain referential integrity.
- Use appropriate indexes.
- Avoid unnecessary duplication.
- Define appropriate entity relationships.
- Use database migrations for schema changes.
- Never store plaintext passwords.
- Protect sensitive patient information.

### Entity Relationships

A simplified conceptual relationship:

```text
User
 |
 |------ UserProfile
 |
 |------ Schedule
 |          |
 |          |------ Medication
 |
 |------ Appointment
 |
 |------ Notification

Hospital
 |
 |------ Doctor
```

Adjust relationships according to actual application requirements.

---

# 7. Authentication & Security

Security is a critical requirement because Medi-Connect handles sensitive patient information.

### Authentication Requirements

- User registration.
- User login.
- JWT-based authentication.
- Password hashing using BCrypt or an appropriate secure encoder.
- Token validation.
- Role-based authorization where required.
- Protected API endpoints.
- Secure logout and token lifecycle management.

### Security Rules

1. Never store plaintext passwords.
2. Never hardcode API keys or secrets.
3. Use environment variables for sensitive configuration.
4. Validate and sanitize incoming data.
5. Protect endpoints against unauthorized access.
6. Apply ownership checks to patient-specific resources.
7. Configure CORS explicitly.
8. Avoid exposing sensitive information in API responses.
9. Protect against common web vulnerabilities.
10. Do not log passwords, tokens, or sensitive medical information.

### Patient Privacy

Treat medical history, allergies, medication records, and personal information as sensitive data.

Only authorized users should be able to access their own records.

---

# 8. API Development Standards

Use versioned REST API endpoints.

Recommended base path:

```text
/api/v1
```

Suggested endpoint organization:

```text
/api/v1/auth
/api/v1/users
/api/v1/profiles
/api/v1/medications
/api/v1/hospitals
/api/v1/doctors
/api/v1/schedules
/api/v1/appointments
/api/v1/notifications
```

### HTTP Method Conventions

| Method | Purpose |
|---|---|
| GET | Retrieve information |
| POST | Create resources |
| PUT | Replace resources |
| PATCH | Partially update resources |
| DELETE | Delete resources |

Ensure that endpoints are logically grouped by module.

---

# 9. Error Handling & Validation

Both frontend and backend must implement proper error handling.

### Backend

- Use global exception handling with `@RestControllerAdvice`.
- Use meaningful exception classes.
- Return appropriate HTTP status codes.
- Validate request DTOs.
- Handle database and external API failures.

### Frontend

- Display meaningful error messages.
- Handle network failures.
- Show loading indicators.
- Validate user inputs.
- Prevent accidental duplicate submissions.
- Handle expired authentication tokens.

Never expose internal stack traces or sensitive implementation details to users.

---

# 10. Testing Standards

Testing must be considered throughout development.

### Backend Testing

Preferred tools:

- JUnit 5
- Mockito
- Spring Boot Test
- MockMvc

Test:

- Controller endpoints.
- Service business logic.
- Repository operations.
- Authentication and authorization.
- Input validation.
- Exception handling.

### Frontend Testing

Preferred tools:

- Vitest or Jest.
- React Testing Library.

Test:

- Component rendering.
- Form validation.
- API integration.
- Authentication flows.
- Dashboard navigation.
- Error and loading states.

Do not consider a feature complete simply because the application compiles.

---

# 11. Development Workflow

Follow this workflow whenever implementing a new feature or modifying existing code.

### Step 1: Understand

- Inspect the existing project structure.
- Identify related components, classes, and services.
- Understand existing dependencies.
- Identify the database entities involved.

### Step 2: Plan

- Explain the proposed implementation.
- Identify affected files.
- Determine API requirements.
- Consider security and validation requirements.

### Step 3: Implement

- Write modular and maintainable code.
- Follow existing naming conventions.
- Reuse existing components and services.
- Avoid unnecessary modifications.

### Step 4: Validate

- Check compilation errors.
- Check frontend build errors.
- Verify API communication.
- Test database operations.
- Verify authentication and authorization.

### Step 5: Explain

After implementation, provide:

1. Summary of changes.
2. Files created.
3. Files modified.
4. Dependencies added, if any.
5. Configuration changes.
6. Instructions to run and test the feature.

---

# 12. Important Instructions for Gemini

You are acting as a senior full-stack software development assistant working on Medi-Connect.

Follow these instructions strictly:

1. Always understand the existing codebase before suggesting modifications.
2. Never assume that a file, class, component, or API already exists.
3. Never rewrite the entire project when only a small modification is required.
4. Preserve existing functionality.
5. Follow the established project architecture.
6. Provide complete and executable code when requested.
7. Include file paths when providing code.
8. Explain important implementation decisions.
9. Do not introduce unnecessary dependencies.
10. Never generate fake API responses as production implementations.
11. Do not leave critical functionality as unexplained placeholders.
12. Consider authentication and authorization in every patient-specific API.
13. Keep frontend and backend responsibilities separate.
14. Do not expose secrets or sensitive patient data.
15. Ask for clarification if a requirement is ambiguous.
16. Do not claim that code has been tested unless it has actually been tested.
17. Prefer incremental development over large, uncontrolled modifications.

### Code Generation Rules

When generating code:

- Provide correct imports.
- Follow the project's existing package naming.
- Include necessary annotations.
- Use appropriate exception handling.
- Include request and response DTOs where necessary.
- Maintain consistent naming conventions.
- Explain required configuration changes.
- Mention any database migration requirements.

---

# 13. Project Development Roadmap

The suggested implementation sequence is:

| Phase | Development Area |
|---|---|
| Phase 1 | Project setup and configuration |
| Phase 2 | Authentication and authorization |
| Phase 3 | Patient profile management |
| Phase 4 | Dashboard development |
| Phase 5 | Hospital and doctor discovery |
| Phase 6 | Personal medication assistant |
| Phase 7 | Medication information module |
| Phase 8 | Notification integration |
| Phase 9 | Testing and security hardening |
| Phase 10 | Dockerization and deployment |

Additional future enhancements:

- AI-powered healthcare information assistant.
- Telemedicine integration.
- Wearable device integration.
- Advanced analytics dashboard.
- Multilingual support.

---

# 14. Final Development Objective

The ultimate goal is to transform Medi-Connect into a complete, production-oriented full-stack healthcare assistance platform.

Every feature should be developed with the following priorities:

1. Security
2. Patient privacy
3. Reliability
4. Maintainability
5. Scalability
6. Performance
7. User experience

**Always remember:** Build Medi-Connect as a real-world software engineering project, not merely a demonstration application. Prioritize clean architecture, reliable functionality, secure data handling, and maintainable code.