
# 🏥 MediConnect

### Your Healthcare Journey, Connected.

**A full-stack healthcare assistance platform for medication awareness,
hospital and doctor discovery, and personal medication organization.**

[![Repository](https://img.shields.io/badge/GitHub-MediConnect-181717?style=for-the-badge&logo=github)](https://github.com/rishavscripts/MediConnect)
[![Frontend](https://img.shields.io/badge/Frontend-React%20%2B%20TypeScript-61DAFB?style=for-the-badge&logo=react&logoColor=black)](https://react.dev/)
[![Backend](https://img.shields.io/badge/Backend-Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Database](https://img.shields.io/badge/Database-PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
:::

------------------------------------------------------------------------

> \[!NOTE\] **Project status:** MediConnect is under active development.
> This README documents the product scope and the intended
> architecture/workflows. A feature or endpoint described as planned
> should not be interpreted as already implemented. Update the
> implementation-status section as the codebase evolves.

## 📌 Contents

-   [About the Project](#-about-the-project)
-   [Problem and Goals](#-problem-and-goals)
-   [Core Modules](#-core-modules)
-   [User Roles](#-user-roles)
-   [System Architecture](#-system-architecture)
-   [End-to-End Application Workflow](#-end-to-end-application-workflow)
-   [Detailed Module Workflows](#-detailed-module-workflows)
-   [Backend Architecture](#-backend-architecture)
-   [Frontend Architecture](#-frontend-architecture)
-   [Database Design](#-database-design)
-   [API Design](#-api-design)
-   [Security and Privacy](#-security-and-privacy)
-   [Technology Stack](#-technology-stack)
-   [Repository Structure](#-repository-structure)
-   [Getting Started](#-getting-started)
-   [Configuration](#-configuration)
-   [Testing Strategy](#-testing-strategy)
-   [Development Roadmap](#-development-roadmap)
-   [Project Documentation](#-project-documentation)
-   [Contributing](#-contributing)
-   [Disclaimer](#-healthcare-disclaimer)


<summary>
```
`<strong>`{=html}✨ Quick project summary --- expand`</strong>`{=html}
```{=html}
</summary>
```
  -----------------------------------------------------------------------
  Area                                Description
  ----------------------------------- -----------------------------------
  Product                             Healthcare assistance web
                                      application

  Frontend                            React, TypeScript, Vite

  Backend                             Java, Spring Boot, Maven

  Database                            PostgreSQL

  API communication                   REST + JSON, versioned under
                                      `/api/v1`

  Architecture                        Modular monolith

  Main user                           Patient / caregiver

  Core capabilities                   Medication information,
                                      hospital/doctor discovery,
                                      medication schedules and
                                      appointment organization
  -----------------------------------------------------------------------


## 🌐 About the Project

**MediConnect** is a healthcare assistance platform designed to bring a
few everyday healthcare-management activities into one accessible
application.

Users should be able to:

1.  Access general, source-attributed medication and health information.
2.  Discover hospitals and doctors using location and specialization
    filters.
3.  Organize medicines according to instructions already provided by a
    qualified healthcare professional.
4.  Keep track of scheduled doses and check-up appointments.
5.  Maintain a personal profile containing relevant information such as
    medical history and allergies.

The application is an **assistance and organization tool**. It is not
intended to replace a doctor, pharmacist, emergency service, diagnosis,
or prescription.

## 🎯 Problem and Goals

Healthcare-related information and personal reminders are often spread
across different applications, websites, and notes. MediConnect aims to
provide one clear starting point for these workflows.

### Product goals

-   Provide a simple and accessible user experience.
-   Support secure registration, authentication, and profile management.
-   Present medication information with reliable sources and clear
    limitations.
-   Help users discover hospitals and doctors.
-   Make it easier to organize existing medication instructions and
    appointments.
-   Protect personal and health-related information through ownership
    checks and access controls.

## 🧩 Core Modules

`<strong>`{=html}1. Medication Assistance`</strong>`{=html}

This module provides general medication-related information and health
guidance based on approved reference sources or reviewed rules.

**Intended workflow** - User opens Medication Assistance. - User enters
a question or relevant symptom information. - The frontend explains that
the result is informational and not a diagnosis. - The backend validates
the input and retrieves relevant information from an approved source. -
The result is displayed with source attribution, limitations, and
guidance on when to seek professional care.

**Safety boundaries** - No autonomous diagnosis. - No autonomous
prescriptions. - No personalized dosage calculation or dosage changes. -
Symptoms must not be used to guess or prescribe a medicine. - Allergy
information and urgent-care guidance must remain visible where relevant.

This module helps users discover healthcare facilities and
professionals.

**Intended capabilities** - Search hospitals by location or name. -
Filter doctors by specialization. - View available hospital and doctor
information. - Display verified address/contact details where
available. - Provide map or route links when supported by the selected
provider.

**Intended workflow** 1. User opens the discovery page. 2. User enters a
location or optionally grants location permission. 3. User selects a
specialization or search term. 4. Frontend sends search filters to the
backend. 5. Backend queries stored records and/or an approved external
provider. 6. Backend normalizes the results. 7. Frontend displays
results and map links where available.


This module helps users organize medication schedules based on an
existing prescription or clinician instruction.

**Intended capabilities** - Add a medication record. - Record the
instructions supplied by the user. - Create daily or weekly schedules. -
View upcoming schedule entries. - Mark a scheduled dose as taken or
skipped. - Review dose history and non-clinical adherence summaries. -
Organize check-up appointments.

**Important:** MediConnect records the instructions supplied by the
user. It must not calculate, invent, or silently modify a medicine dose
or schedule.


## 👥 User Roles

  -----------------------------------------------------------------------
  Role                                Intended access
  ----------------------------------- -----------------------------------
  Visitor                             Public landing, About, Contact,
                                      registration, and login

  Patient                             Own profile, medical information,
                                      medication assistance, discovery,
                                      schedules, appointments, and own
                                      notifications

  Administrator                       Only explicitly authorized
                                      platform-management functions
  -----------------------------------------------------------------------

Access to a patient's medical information must not be granted to an
administrator by default. Permissions should follow the principle of
least privilege.

## 🏗️ System Architecture

MediConnect follows a **modular monolith** approach. The frontend and
backend are separate applications, while the backend keeps related
business capabilities organized into modules inside one Spring Boot
application.

``` mermaid
flowchart TB
    U["User / Browser"] --> FE["React + TypeScript + Vite"]
    FE -->|"HTTPS · REST · JSON"| API["Spring Boot REST API"]

    subgraph BACKEND["Backend — Modular Monolith"]
        API --> SEC["Authentication & Authorization"]
        API --> PROF["User & Profile Module"]
        API --> MED["Medication Information Module"]
        API --> DISC["Hospital & Doctor Discovery Module"]
        API --> ASSIST["Medication Schedule & Appointment Module"]
        API --> NOTIF["Notification Module — planned"]
    end

    SEC --> DB[("PostgreSQL")]
    PROF --> DB
    MED --> DB
    DISC --> DB
    ASSIST --> DB
    NOTIF --> DB

    MED -. "Approved reference source" .-> EXT1["Medication Information Provider"]
    DISC -. "Maps / Places provider" .-> EXT2["Location & Maps Provider"]
```

### Architecture principles

-   **Separation of concerns:** UI, API, business logic, and persistence
    have distinct responsibilities.
-   **Modularity:** Each business capability belongs to a clear backend
    module.
-   **REST conventions:** Frontend and backend communicate through
    versioned JSON APIs.
-   **Data ownership:** Patient-specific records are associated with the
    authenticated user.
-   **External integrations:** Third-party services are accessed through
    backend integration/service layers.
-   **Start simple:** Avoid microservices until real scaling or
    operational needs justify them.

### Request lifecycle

``` mermaid
sequenceDiagram
    autonumber
    actor User
    participant UI as React Frontend
    participant API as Spring Boot Controller
    participant Service as Business Service
    participant Repo as Spring Data Repository
    participant DB as PostgreSQL

    User->>UI: Submit an action
    UI->>API: HTTP request + JSON
    API->>API: Validate request and identity
    API->>Service: Pass validated input
    Service->>Service: Apply business rules
    Service->>Repo: Read / write data
    Repo->>DB: SQL operation
    DB-->>Repo: Stored data / query result
    Repo-->>Service: Entity / result
    Service-->>API: Response DTO
    API-->>UI: HTTP status + JSON
    UI-->>User: Render success, data, or error state
```

## 🔄 End-to-End Application Workflow

``` mermaid
flowchart TD
    A([Open MediConnect]) --> B[Public Landing Page]
    B --> C{Has an account?}
    C -- No --> D[Register]
    C -- Yes --> E[Login]
    D --> F[Backend validates registration]
    F --> G[Create account and securely store password hash]
    G --> E
    E --> H{Credentials valid?}
    H -- No --> I[Show safe error message]
    I --> E
    H -- Yes --> J[Establish authenticated access]
    J --> K[Patient Dashboard]
    K --> L[Medication Assistance]
    K --> M[Hospital & Doctor Discovery]
    K --> N[Personal Medication Assistant]
    K --> O[Profile & Medical History]
    L --> P[View sourced information]
    M --> Q[Review facilities and professionals]
    N --> R[Manage schedules and appointments]
    O --> S[Manage own profile and health details]
```

## 🔍 Detailed Module Workflows

### 1. Registration and login

``` mermaid
flowchart TD
    A[Visitor selects Sign Up] --> B[Enter required account details]
    B --> C[Frontend validation]
    C --> D[POST /api/v1/auth/register]
    D --> E[Backend validation]
    E --> F{Account details valid and unique?}
    F -- No --> G[Return validation / conflict response]
    F -- Yes --> H[Hash password]
    H --> I[Persist user account]
    I --> J[Return safe response]
    J --> K[User logs in]
    K --> L[Verify credentials]
    L --> M{Valid?}
    M -- No --> N[Return authentication error]
    M -- Yes --> O[Issue authenticated access]
    O --> P[Open protected dashboard]
```

### 2. Hospital and doctor discovery

``` mermaid
flowchart TD
    A[Open discovery] --> B[Enter location]
    B --> C[Choose specialization / search term]
    C --> D[Send search request]
    D --> E[Backend validates filters]
    E --> F[Query database or approved provider]
    F --> G[Normalize and filter results]
    G --> H{Results available?}
    H -- Yes --> I[Display result cards]
    I --> J[View details / map link]
    H -- No --> K[Display empty state]
    E --> L[Handle provider or validation error]
```

### 3. Add and manage a medication schedule

``` mermaid
flowchart TD
    A[Open Medication Assistant] --> B[Add medication]
    B --> C[Enter existing clinician-provided instructions]
    C --> D[Enter schedule and timezone]
    D --> E[Backend validates input]
    E --> F{Valid?}
    F -- No --> G[Show validation errors]
    F -- Yes --> H[Save medication record]
    H --> I[Save schedule]
    I --> J[Show upcoming schedule]
    J --> K[User records dose status]
    K --> L{Taken or skipped?}
    L -- Taken --> M[Persist TAKEN event]
    L -- Skipped --> N[Persist SKIPPED event]
    M --> O[Update history]
    N --> O
```

### 4. Appointment organization

``` mermaid
flowchart TD
    A[Open Appointments] --> B[Add check-up appointment]
    B --> C[Enter date, time, and optional provider]
    C --> D[Backend validates ownership and date/time]
    D --> E{Valid?}
    E -- No --> F[Return validation error]
    E -- Yes --> G[Persist appointment]
    G --> H[Display upcoming appointment]
    H --> I[User views, updates, or cancels own appointment]
```

### 5. Notification lifecycle

> In-app reminders are the intended first step. Email and push
> notification integrations are not part of the initial implementation
> scope.

``` mermaid
flowchart TD
    A[Medication schedule / appointment saved] --> B[Create reminder event]
    B --> C[Due-time worker checks pending events]
    C --> D{Reminder is due?}
    D -- No --> C
    D -- Yes --> E[Create in-app notification]
    E --> F[Record delivery state]
    F --> G[Display notification to its owner]
```

Implementation considerations: - Prevent duplicate reminders with unique
constraints or idempotency keys. - Store and evaluate the user's
timezone. - Keep notification records scoped to their owner. - Handle
schedule edits and cancellations so obsolete reminders are not
delivered.

## ⚙️ Backend Architecture

The backend is planned as a layered Spring Boot application.

``` mermaid
flowchart TB
    C["Controller Layer<br/>HTTP endpoints, request validation"] --> S["Service Layer<br/>Business rules and orchestration"]
    S --> R["Repository Layer<br/>Spring Data JPA"]
    R --> E[("PostgreSQL")]
    C -.-> DTO["Request / Response DTOs"]
    S -.-> ENT["JPA Entities"]
    C -.-> EX["Global Exception Handling"]
    CFG["Configuration & Security"] -.-> C
    CFG -.-> S
```

### Layer responsibilities

  -----------------------------------------------------------------------
  Layer                               Responsibility
  ----------------------------------- -----------------------------------
  Controller                          Maps HTTP requests, validates
                                      input, returns suitable status
                                      codes

  DTO                                 Defines API request and response
                                      contracts

  Service                             Contains business rules and
                                      coordinates operations

  Repository                          Performs database access using
                                      Spring Data JPA

  Entity                              Represents persisted domain data

  Security / Config                   Authentication, authorization,
                                      CORS, and application configuration

  Exception handling                  Converts expected failures into
                                      consistent API responses
  -----------------------------------------------------------------------

### Proposed backend module boundaries

-   `auth` --- registration, login, logout, token/session lifecycle.
-   `user` --- user account, profile, medical history, and allergies.
-   `medication` --- medication reference information and source
    metadata.
-   `hospital` --- hospital and doctor discovery data.
-   `assistant` --- user medication records, schedules, dose events, and
    appointments.
-   `notification` --- in-app notification records and reminder
    processing.
-   `common` --- shared response models, validation, and exception
    handling.

Each module can contain `controller`, `service`, `repository`, `dto`,
and `entity` packages where appropriate.

## 🖥️ Frontend Architecture

The frontend uses React with TypeScript and Vite. Components should
remain reusable, while API calls should be grouped in service modules
rather than scattered throughout page components.

``` mermaid
flowchart TD
    APP["App / Router"] --> PUB["Public Pages"]
    APP --> AUTH["Authentication Pages"]
    APP --> PROT["Protected Application Layout"]
    PROT --> DASH["Dashboard"]
    PROT --> PROF["Profile"]
    PROT --> MED["Medication Assistance"]
    PROT --> DISC["Hospital / Doctor Discovery"]
    PROT --> ASSIST["Medication Assistant"]
    PROT --> APPT["Appointments"]
    DASH --> API["API Service Layer"]
    PROF --> API
    MED --> API
    DISC --> API
    ASSIST --> API
    APPT --> API
    API --> BE["Spring Boot REST API"]
```

### Frontend responsibilities

-   Route users to public and authenticated pages.
-   Keep authentication state and protected-route behavior consistent.
-   Validate forms before sending requests.
-   Use typed request and response models.
-   Centralize API configuration and error handling.
-   Display loading, empty, success, validation, and failure states.
-   Keep sensitive information out of unnecessary UI surfaces.

## 🗄️ Database Design

PostgreSQL is the relational data store. The logical schema is designed
around user ownership and clear relationships. The schema document is a
**proposed design** and should be kept synchronized with the actual
migrations and entities.

``` mermaid
erDiagram
    USERS ||--o| USER_PROFILES : has
    USERS ||--o{ MEDICAL_CONDITIONS : records
    USERS ||--o{ ALLERGIES : records
    USERS ||--o{ USER_MEDICATIONS : manages
    MEDICATION_CATALOG o|--o{ USER_MEDICATIONS : references
    USER_MEDICATIONS ||--o{ MEDICATION_SCHEDULES : has
    MEDICATION_SCHEDULES ||--o{ DOSE_EVENTS : generates
    USERS ||--o{ APPOINTMENTS : owns
    HOSPITALS o|--o{ DOCTORS : includes
    HOSPITALS o|--o{ APPOINTMENTS : associated_with
    DOCTORS o|--o{ APPOINTMENTS : associated_with
    USERS ||--o{ NOTIFICATIONS : receives

    USERS {
        uuid id PK
        string full_name
        string email UK
        string password_hash
        string role
        boolean is_active
    }
    USER_PROFILES {
        uuid id PK
        uuid user_id FK
        date date_of_birth
        string phone
        string address
    }
    MEDICAL_CONDITIONS {
        uuid id PK
        uuid user_id FK
        string condition_name
        text notes
    }
    ALLERGIES {
        uuid id PK
        uuid user_id FK
        string substance
        text reaction_notes
    }
    HOSPITALS {
        uuid id PK
        string name
        text address
        decimal latitude
        decimal longitude
    }
    DOCTORS {
        uuid id PK
        uuid hospital_id FK
        string name
        string specialization
    }
    MEDICATION_CATALOG {
        uuid id PK
        string name
        string generic_name
        string source_name
    }
    USER_MEDICATIONS {
        uuid id PK
        uuid user_id FK
        uuid catalog_id FK
        string display_name
        text instruction_text
    }
    MEDICATION_SCHEDULES {
        uuid id PK
        uuid user_medication_id FK
        time time_of_day
        string timezone
        date start_date
        date end_date
    }
    DOSE_EVENTS {
        uuid id PK
        uuid schedule_id FK
        datetime scheduled_for
        string status
    }
    APPOINTMENTS {
        uuid id PK
        uuid user_id FK
        uuid hospital_id FK
        uuid doctor_id FK
        datetime appointment_at
        string status
    }
    NOTIFICATIONS {
        uuid id PK
        uuid user_id FK
        string type
        string status
    }
```

### Main entities

  -----------------------------------------------------------------------
  Entity                              Purpose
  ----------------------------------- -----------------------------------
  `users`                             Account identity, role, and
                                      password hash

  `user_profiles`                     Basic profile details

  `medical_conditions`                User-recorded medical history

  `allergies`                         User-recorded allergies

  `hospitals`                         Healthcare facility details and
                                      location

  `doctors`                           Doctor details and specialization

  `medication_catalog`                Reference information with source
                                      attribution

  `user_medications`                  A user's own medication record and
                                      supplied instructions

  `medication_schedules`              Timing and recurrence for a
                                      user-entered schedule

  `dose_events`                       Scheduled dose history and status

  `appointments`                      User's check-up appointments

  `notifications`                     User-scoped in-app notification
                                      records
  -----------------------------------------------------------------------

### Data design guidelines

-   Use UUID identifiers for externally referenced records.
-   Use foreign keys and indexes for integrity and query performance.
-   Store timestamps consistently in UTC; retain the user's timezone for
    schedule interpretation.
-   Never store plaintext passwords, access tokens, or provider secrets.
-   Keep patient-owned data linked to its owner.
-   Use database migrations (Flyway is the planned migration tool) to
    version schema changes.

## 🔌 API Design

The planned API base path is:

``` text
/api/v1
```

The following routes describe the intended API contract. They are
**design examples**, not a claim that every endpoint already exists.

  --------------------------------------------------------------------------------------------
  Module            Method            Example endpoint                       Purpose
  ----------------- ----------------- -------------------------------------- -----------------
  Auth              `POST`            `/auth/register`                       Create an account

  Auth              `POST`            `/auth/login`                          Authenticate a
                                                                             user

  Auth              `POST`            `/auth/refresh`                        Refresh
                                                                             authenticated
                                                                             access, if
                                                                             enabled

  Auth              `POST`            `/auth/logout`                         End authenticated
                                                                             access

  Profile           `GET`             `/users/me`                            Get current
                                                                             user's profile

  Profile           `PATCH`           `/users/me`                            Update permitted
                                                                             profile fields

  Medical profile   `GET`             `/users/me/medical-profile`            Read own medical
                                                                             profile

  Hospitals         `GET`             `/hospitals`                           Search hospitals

  Doctors           `GET`             `/doctors`                             Search/filter
                                                                             doctors

  Medication info   `GET`             `/medications/search`                  Search reference
                                                                             information

  User medications  `GET`             `/assistant/medications`               List own
                                                                             medication
                                                                             records

  User medications  `POST`            `/assistant/medications`               Add a medication
                                                                             record

  Schedules         `GET`             `/assistant/schedules`                 List own
                                                                             schedules

  Schedules         `POST`            `/assistant/schedules`                 Create a schedule

  Dose events       `POST`            `/assistant/dose-events/{id}/status`   Record a dose
                                                                             status

  Appointments      `GET`             `/assistant/appointments`              List own
                                                                             appointments

  Appointments      `POST`            `/assistant/appointments`              Add an
                                                                             appointment

  Notifications     `GET`             `/notifications`                       List own
                                                                             notifications

  Notifications     `PATCH`           `/notifications/{id}/read`             Mark an owned
                                                                             notification as
                                                                             read
  --------------------------------------------------------------------------------------------

### API conventions

-   Use nouns for resource paths and standard HTTP methods.
-   Validate all incoming data on the backend.
-   Use DTOs rather than exposing persistence entities directly.
-   Return consistent error response structures.
-   Use suitable HTTP status codes (`200`, `201`, `204`, `400`, `401`,
    `403`, `404`, `409`, `500` as appropriate).
-   Apply pagination to list endpoints as data grows.
-   Enforce ownership checks on every patient-specific operation.

## 🔐 Security and Privacy

Healthcare-related information is sensitive. Security must be part of
the design from the beginning.

-   Hash passwords using a modern password-hashing mechanism such as
    BCrypt.
-   Use Spring Security for authentication and authorization.
-   Use a maintained JWT implementation if JWT is selected for the
    authentication design.
-   Protect patient routes and APIs; frontend route guards alone are not
    security.
-   Verify record ownership in backend services, not only in the UI.
-   Keep secrets in environment variables and never commit real
    credentials.
-   Restrict CORS to trusted frontend origins.
-   Avoid logging passwords, tokens, or sensitive medical details.
-   Collect only the personal information needed for a clear product
    purpose.
-   Provide safe error messages without leaking whether sensitive
    account details exist.
-   Keep external provider keys on the backend whenever possible.

## 🧰 Technology Stack

  -----------------------------------------------------------------------
  Layer                   Technology              Purpose
  ----------------------- ----------------------- -----------------------
  Frontend                React                   Component-based user
                                                  interface

  Language                TypeScript              Typed frontend
                                                  development

  Build tool              Vite                    Frontend development
                                                  and build

  Backend                 Java + Spring Boot      REST API and business
                                                  logic

  Build tool              Maven                   Backend dependency and
                                                  build management

  Persistence             Spring Data JPA /       Relational data access
                          Hibernate               

  Database                PostgreSQL              Persistent application
                                                  data

  API                     REST + JSON             Frontend/backend
                                                  communication

  Security                Spring Security +       Authentication and
                          password hashing; JWT   authorization
                          planned                 

  Database migration      Flyway planned          Version-controlled
                                                  schema changes

  Testing                 JUnit / Mockito; Vitest Backend and frontend
                          / React Testing Library testing
                          planned                 

  External services       Maps and approved       Discovery and reference
                          medication-reference    information
                          providers               
  -----------------------------------------------------------------------

> The table describes the selected/planned stack. Check the actual
> `pom.xml` and frontend `package.json` before assuming a dependency or
> integration is already configured.

## 📁 Repository Structure

The repository currently separates the application into `backend/` and
`frontend/`, alongside planning and product documentation.

``` text
MediConnect/
│
├── backend/                 # Spring Boot application
├── frontend/                # React + TypeScript + Vite application
│
├── GEMINI.md                # AI-assisted development instructions
├── PRD.md                   # Product requirements and scope
├── TechSpec.md              # Technical architecture and conventions
├── AppFlow.md               # User journeys and application workflows
├── Design.md                # UI and design-system guidance
├── Schema.md                # Proposed database schema
├── Tracker.md               # Development progress and tasks
├── Rules.md                 # Project rules and implementation constraints
│
├── .gitignore
└── README.md
```

Additional folders such as `database/`, `docs/`, `deployment/`, and
`scripts/` may be introduced as implementation needs arise. The tree
above intentionally avoids claiming that unverified folders or files
already exist.

## 🚀 Getting Started

### Prerequisites

Install the following tools before running the project:

-   Git
-   Java version required by the backend's `pom.xml`
-   Maven (or the Maven wrapper, if included)
-   Node.js LTS and npm
-   PostgreSQL

### 1. Clone the repository

``` bash
git clone https://github.com/rishavscripts/MediConnect.git
cd MediConnect
```

### 2. Configure PostgreSQL

Create the project database:

``` sql
CREATE DATABASE "MediConnect";
```

Make sure the PostgreSQL service is running. Use the database name,
username, and password configured for your local environment.

### 3. Configure the backend

Open the `backend/` folder and inspect its configuration files. Add the
PostgreSQL connection settings and any required secrets to your local
environment/configuration.

Do not commit credentials. Use the actual property names expected by the
current backend configuration; see [Configuration](#-configuration).

### 4. Start the backend

From the backend directory, use the command supported by the project:

``` bash
# Maven
mvn spring-boot:run

# If the Maven wrapper is present (Windows)
mvnw.cmd spring-boot:run

# If the Maven wrapper is present (macOS / Linux)
./mvnw spring-boot:run
```

The backend port is determined by the active Spring Boot configuration.

### 5. Start the frontend

Open a second terminal:

``` bash
cd frontend
npm install
npm run dev
```

Vite prints the local frontend URL in the terminal after startup.

### 6. Verify the application

-   Confirm that PostgreSQL is running.
-   Confirm that the backend starts without configuration or migration
    errors.
-   Confirm that the frontend starts successfully.
-   Check the browser console and backend logs for failed API requests.
-   Verify that the frontend API base URL points to the local backend.

## ⚙️ Configuration

Configuration keys can differ as the implementation evolves. Use the
actual names in the backend's `application.properties` /
`application.yml` and frontend environment files.

Typical values that the application may need:

  ------------------------------------------------------------------------------------------------
  Configuration           Purpose                 Example / guidance
  ----------------------- ----------------------- ------------------------------------------------
  Database URL            PostgreSQL connection   `jdbc:postgresql://localhost:5432/MediConnect`

  Database username       Local PostgreSQL user   Your local DB username

  Database password       Local PostgreSQL        Keep private
                          password                

  Frontend API base URL   Backend API origin +    `http://localhost:<backend-port>/api/v1`
                          version prefix          

  JWT secret/key          Signing authenticated   Generate a strong secret; never commit it
                          tokens, if JWT is       
                          implemented             

  Maps provider key       Maps/location           Keep server-side when possible
                          integration, if         
                          required                

  Medication data         Approved reference      Keep server-side when possible
  provider key            integration, if         
                          required                
  ------------------------------------------------------------------------------------------------

**Environment-file checklist**

-   Keep `.env` and local secret files out of Git.
-   Provide safe example files such as `.env.example` with placeholders
    only.
-   Validate required configuration at application startup.
-   Never paste real credentials into issues, commits, screenshots, or
    documentation.

## 🧪 Testing Strategy

Testing should cover the important business rules and the boundaries
between frontend, backend, database, and external providers.

`<strong>`{=html}Backend testing checklist`</strong>`{=html}

-   [ ] Unit-test service-layer business rules.
-   [ ] Test controller request validation and response codes.
-   [ ] Test repository queries and entity relationships.
-   [ ] Test authentication and authorization.
-   [ ] Verify that one user cannot access another user's records.
-   [ ] Test invalid and expired schedule data.
-   [ ] Test dose-event state transitions.
-   [ ] Test appointment ownership and validation.
-   [ ] Test global exception handling.
-   [ ] Test external-provider failures and timeouts.

`<strong>`{=html}Frontend testing checklist`</strong>`{=html}

-   [ ] Test registration and login form validation.
-   [ ] Test protected routes and unauthenticated redirects.
-   [ ] Test loading, empty, success, and error states.
-   [ ] Test hospital/doctor search filters.
-   [ ] Test medication schedule forms.
-   [ ] Test appointment creation and editing.
-   [ ] Test responsive layouts and keyboard accessibility.
-   [ ] Test API error handling.

`<strong>`{=html}End-to-end acceptance checklist`</strong>`{=html}

-   [ ] A visitor can access public pages.
-   [ ] A user can register and authenticate.
-   [ ] A user can access only their own protected records.
-   [ ] Hospital/doctor discovery handles denied location permission.
-   [ ] Medication information displays its source and limitations.
-   [ ] A schedule can be created from user-supplied clinician
    instructions.
-   [ ] Dose status changes are saved and visible in history.
-   [ ] Appointment records can be viewed and managed by their owner.
-   [ ] Duplicate reminders are prevented when reminder processing is
    implemented.

## 🗺️ Development Roadmap

The roadmap below represents a suggested implementation sequence. Update
it to match the current state recorded in `Tracker.md`.

``` mermaid
flowchart LR
    P1["Phase 1<br/>Project foundation"] --> P2["Phase 2<br/>Authentication & profile"]
    P2 --> P3["Phase 3<br/>Core database & APIs"]
    P3 --> P4["Phase 4<br/>Medication assistance"]
    P4 --> P5["Phase 5<br/>Hospital & doctor discovery"]
    P5 --> P6["Phase 6<br/>Medication schedules"]
    P6 --> P7["Phase 7<br/>Appointments & in-app reminders"]
    P7 --> P8["Phase 8<br/>Testing, security & deployment"]
```

  -----------------------------------------------------------------------
  Phase                   Focus                   Expected outcome
  ----------------------- ----------------------- -----------------------
  1                       Foundation              Repository setup,
                                                  frontend/backend
                                                  startup, PostgreSQL
                                                  connection

  2                       Authentication &        Registration, login,
                          profile                 protected routes, user
                                                  profile

  3                       Core data layer         Entities, repositories,
                                                  migrations, validation,
                                                  error handling

  4                       Medication assistance   Safe information
                                                  workflow and source
                                                  attribution

  5                       Discovery               Hospital/doctor search
                                                  and location
                                                  integration

  6                       Medication assistant    Medication records,
                                                  schedules, dose history

  7                       Appointments &          Check-up tracking and
                          reminders               in-app reminder
                                                  foundation

  8                       Quality & release       Tests, security review,
                                                  documentation,
                                                  deployment
  -----------------------------------------------------------------------

## 📚 Project Documentation

The repository includes dedicated documents so product requirements,
architecture, workflows, and implementation rules remain clear.

  -----------------------------------------------------------------------
  Document                            What it covers
  ----------------------------------- -----------------------------------
  [`PRD.md`](PRD.md)                  Product vision, goals, scope,
                                      requirements, and exclusions

  [`TechSpec.md`](TechSpec.md)        Technical stack, architecture,
                                      package boundaries, and API
                                      conventions

  [`AppFlow.md`](AppFlow.md)          Actors, user journeys, module
                                      workflows, and route access

  [`Schema.md`](Schema.md)            Proposed relational data model and
                                      database relationships

  [`Design.md`](Design.md)            Visual language, UI patterns,
                                      accessibility, and responsive
                                      behavior

  [`Tracker.md`](Tracker.md)          Tasks, implementation progress, and
                                      blockers

  [`Rules.md`](Rules.md)              Project-wide development
                                      constraints

  [`GEMINI.md`](GEMINI.md)            Context and instructions for
                                      AI-assisted development
  -----------------------------------------------------------------------

## 🤝 Contributing

Contributions and suggestions are welcome.

1.  Fork the repository.
2.  Create a focused branch: `git checkout -b feature/your-feature`.
3.  Make changes that follow the project rules and architecture.
4.  Add or update tests for changed behavior.
5.  Update documentation when workflows, endpoints, or schema change.
6.  Commit with a clear message.
7.  Open a pull request describing the change and how it was tested.

## 🩺 Healthcare Disclaimer

MediConnect is intended for healthcare assistance, information
organization, and discovery. It does **not** provide medical diagnosis,
prescribe medication, calculate personalized dosages, or replace advice
from qualified healthcare professionals. Users should follow their
clinician's instructions and consult a qualified professional about
medical questions. For urgent or emergency situations, contact local
emergency services or seek immediate professional care.

------------------------------------------------------------------------


**Built with the goal of making everyday healthcare management more
organized and accessible.**

[⬆ Back to Top](#-mediconnect)

