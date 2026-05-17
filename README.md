# Return Management System — Backend API

A production-ready REST API for managing product returns, non-conformities, and quality tracking. Built with Spring Boot 3 and secured with JWT authentication.

## Live Application

**[https://gestion-retours-frontend-176480887870.europe-west1.run.app](https://gestion-retours-frontend-176480887870.europe-west1.run.app)**

> The frontend is deployed on Google Cloud Run and consumes this API.

---

## Features

- **JWT Authentication** — Stateless, token-based auth with role-based access control (ADMIN, QUALITE, EMPLOYE)
- **Product Return Lifecycle** — Full state machine: `EN_ATTENTE → EN_COURS → VALIDE / REJETE / TRAITE`
- **Non-Conformity Management** — Create and track quality non-conformities with severity levels (FAIBLE, MOYENNE, HAUTE, CRITIQUE)
- **Audit Trail** — Every state change is automatically recorded in the history
- **Dashboard Statistics** — Aggregated metrics for quality monitoring
- **Email Alerts** — Automatic notifications for critical/high-severity non-conformities
- **Swagger UI** — Interactive API documentation at `/swagger-ui.html`

---

## Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| Java | 17 | Programming language |
| Spring Boot | 3.2.5 | Application framework |
| Spring Security | 6.x | Authentication & authorization |
| Spring Data JPA | 3.x | ORM / database access |
| PostgreSQL (Neon) | Cloud | Database |
| jjwt | 0.11.5 | JWT token generation & validation |
| Lombok | latest | Boilerplate reduction |
| SpringDoc OpenAPI | 2.3.0 | Swagger UI documentation |
| Maven | 3.9 | Build tool |
| Docker | latest | Containerization |

---

## Prerequisites

- Java 17+
- Maven 3.9+ (or use the included `mvnw` wrapper)
- A PostgreSQL database (connection string required via environment variable)

---

## Getting Started

```bash
# Clone the repository
git clone https://github.com/syrinemrf/ReturnManagementSystem_Backend.git
cd ReturnManagementSystem_Backend

# Set required environment variables (see section below), then run:
./mvnw spring-boot:run          # Linux / Mac
mvnw.cmd spring-boot:run        # Windows
```

The API will be available at **http://localhost:8080**.

### Run with Docker

```bash
# Build and start all services
docker-compose up --build

# Run in background
docker-compose up -d --build

# Stop
docker-compose down
```

---

## Environment Variables

All sensitive configuration is provided via environment variables — nothing is hardcoded.

| Variable | Description | Default |
|----------|-------------|---------|
| `SPRING_DATASOURCE_URL` | PostgreSQL JDBC connection URL | — |
| `SPRING_DATASOURCE_USERNAME` | Database username | — |
| `SPRING_DATASOURCE_PASSWORD` | Database password | — |
| `APP_JWT_SECRET` | JWT signing secret (min. 256-bit) | — |
| `APP_JWT_EXPIRATION` | JWT expiry in milliseconds | `86400000` (24h) |
| `APP_CORS_ALLOWED_ORIGINS` | Comma-separated allowed origins | `http://localhost:4200` |
| `MAIL_USERNAME` | SMTP email address for alerts | — |
| `MAIL_PASSWORD` | SMTP email password | — |
| `MAIL_ALERT_RECIPIENT` | Recipient for critical NC alerts | — |
| `SERVER_PORT` | Application port | `8080` |

For local development, create `src/main/resources/application-local.properties` and set the variables there. This file is gitignored.

---

## API Endpoints

All endpoints are prefixed with `/api`. Authentication endpoints are public; all others require a valid `Bearer` token.

### Authentication — `/api/auth`

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/api/auth/login` | Login — returns JWT token | Public |
| POST | `/api/auth/register` | Register a new user | Public |

### Product Returns — `/api/retours`

| Method | Endpoint | Description | Role |
|--------|----------|-------------|------|
| GET | `/api/retours` | List all returns | Any |
| GET | `/api/retours/{id}` | Get return by ID | Any |
| GET | `/api/retours/etat/{etat}` | Filter by state | Any |
| POST | `/api/retours` | Create a return | EMPLOYE, QUALITE, ADMIN |
| PUT | `/api/retours/{id}` | Update a return | QUALITE, ADMIN |
| PUT | `/api/retours/{id}/etat` | Change return state | QUALITE, ADMIN |
| DELETE | `/api/retours/{id}` | Delete a return | ADMIN |

### Non-Conformities — `/api/non-conformites`

| Method | Endpoint | Description | Role |
|--------|----------|-------------|------|
| GET | `/api/non-conformites` | List all | Any |
| GET | `/api/non-conformites/{id}` | Get by ID | Any |
| GET | `/api/non-conformites/retour/{retourId}` | By return | Any |
| POST | `/api/non-conformites` | Create | QUALITE, ADMIN |
| PUT | `/api/non-conformites/{id}` | Update | QUALITE, ADMIN |
| DELETE | `/api/non-conformites/{id}` | Delete | ADMIN |

### Users — `/api/utilisateurs`

| Method | Endpoint | Description | Role |
|--------|----------|-------------|------|
| GET | `/api/utilisateurs` | List all users | ADMIN |
| GET | `/api/utilisateurs/{id}` | Get user by ID | ADMIN |
| POST | `/api/utilisateurs` | Create user | ADMIN |
| PUT | `/api/utilisateurs/{id}` | Update user | ADMIN |
| DELETE | `/api/utilisateurs/{id}` | Delete user | ADMIN |

### Dashboard — `/api/dashboard`

| Method | Endpoint | Description | Role |
|--------|----------|-------------|------|
| GET | `/api/dashboard/stats` | Aggregated statistics | QUALITE, ADMIN |

### History — `/api/historique`

| Method | Endpoint | Description | Role |
|--------|----------|-------------|------|
| GET | `/api/historique/retour/{retourId}` | History for a return | Any |
| GET | `/api/historique/recent` | Recent activity feed | Any |

---

## Swagger UI

Access the interactive API documentation at:

**http://localhost:8080/swagger-ui.html**

To authenticate:
1. Call `POST /api/auth/login` with your credentials
2. Copy the `token` from the response
3. Click **Authorize** at the top of the Swagger page
4. Enter `Bearer <your_token>` and confirm

---

## Default Credentials

> Change these before deploying to production.

| Role | Email | Password |
|------|-------|----------|
| ADMIN | admin@retours.com | Admin123! |
| QUALITE | qualite@retours.com | Qualite123! |
| EMPLOYE | employe@retours.com | Employe123! |

---

## Related Repository

- **Frontend:** [RaturnManagementSystem_Frontend](https://github.com/syrinemrf/RaturnManagementSystem_Frontend) — Live at [gestion-retours-frontend-176480887870.europe-west1.run.app](https://gestion-retours-frontend-176480887870.europe-west1.run.app)

---

## License

MIT License
