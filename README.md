# Return Management System: Backend API

A production-ready REST API for managing product returns, non-conformities, and quality tracking. Built with Spring Boot 3 and secured with JWT authentication.

## Live Application

**[https://gestion-retours-frontend-176480887870.europe-west1.run.app](https://gestion-retours-frontend-176480887870.europe-west1.run.app)**

The frontend is deployed on Google Cloud Run and consumes this API.

## Features

- Stateless JWT authentication with role-based access control (ADMIN, QUALITE, EMPLOYE)
- Product return lifecycle management with a defined state machine: `EN_ATTENTE` to `EN_COURS` to `VALIDE`, `REJETE`, or `TRAITE`
- Non-conformity tracking with severity levels: FAIBLE, MOYENNE, HAUTE, CRITIQUE
- Automatic audit trail on every state change
- Aggregated dashboard statistics for quality monitoring
- Automatic email alerts for critical and high-severity non-conformities

## Role-Based Access

The API enforces strict access control based on three roles:

| Role | Permissions |
|------|-------------|
| **ADMIN** | Full access: user management, all CRUD operations, deletions |
| **QUALITE** | Create and manage non-conformities, update return states, view dashboard |
| **EMPLOYE** | Submit new returns and view data; no state changes or non-conformity management |

All protected endpoints require a valid `Bearer` token in the `Authorization` header.

## Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| Java | 17 | Programming language |
| Spring Boot | 3.2.5 | Application framework |
| Spring Security | 6.x | Authentication and authorization |
| Spring Data JPA | 3.x | ORM / database access |
| PostgreSQL (Neon) | Cloud | Database |
| jjwt | 0.11.5 | JWT token generation and validation |
| Lombok | latest | Boilerplate reduction |
| Maven | 3.9 | Build tool |
| Docker | latest | Containerization |

## Prerequisites

- Java 17+
- Maven 3.9+ (or use the included `mvnw` wrapper)
- A PostgreSQL database (connection string required via environment variable)

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

## Environment Variables

All sensitive configuration is provided via environment variables with no hardcoded secrets.

| Variable | Description | Default |
|----------|-------------|---------|
| `SPRING_DATASOURCE_URL` | PostgreSQL JDBC connection URL | (required) |
| `SPRING_DATASOURCE_USERNAME` | Database username | (required) |
| `SPRING_DATASOURCE_PASSWORD` | Database password | (required) |
| `APP_JWT_SECRET` | JWT signing secret (min. 256-bit) | (required) |
| `APP_JWT_EXPIRATION` | JWT expiry in milliseconds | `86400000` (24h) |
| `APP_CORS_ALLOWED_ORIGINS` | Comma-separated allowed origins | `http://localhost:4200` |
| `MAIL_USERNAME` | SMTP email address for alerts | (required) |
| `MAIL_PASSWORD` | SMTP email password | (required) |
| `MAIL_ALERT_RECIPIENT` | Recipient for critical NC alerts | (required) |
| `SERVER_PORT` | Application port | `8080` |

For local development, create `src/main/resources/application-local.properties` and set the variables there. This file is gitignored.

## API Endpoints

All endpoints are prefixed with `/api`. Authentication endpoints are public; all others require a valid Bearer token.

### Authentication

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/api/auth/login` | Login and receive a JWT token | Public |
| POST | `/api/auth/register` | Register a new user | Public |

### Product Returns

| Method | Endpoint | Description | Role |
|--------|----------|-------------|------|
| GET | `/api/retours` | List all returns | Any |
| GET | `/api/retours/{id}` | Get return by ID | Any |
| GET | `/api/retours/etat/{etat}` | Filter by state | Any |
| POST | `/api/retours` | Create a return | EMPLOYE, QUALITE, ADMIN |
| PUT | `/api/retours/{id}` | Update a return | QUALITE, ADMIN |
| PUT | `/api/retours/{id}/etat` | Change return state | QUALITE, ADMIN |
| DELETE | `/api/retours/{id}` | Delete a return | ADMIN |

### Non-Conformities

| Method | Endpoint | Description | Role |
|--------|----------|-------------|------|
| GET | `/api/non-conformites` | List all | Any |
| GET | `/api/non-conformites/{id}` | Get by ID | Any |
| GET | `/api/non-conformites/retour/{retourId}` | List by return | Any |
| POST | `/api/non-conformites` | Create | QUALITE, ADMIN |
| PUT | `/api/non-conformites/{id}` | Update | QUALITE, ADMIN |
| DELETE | `/api/non-conformites/{id}` | Delete | ADMIN |

### Users

| Method | Endpoint | Description | Role |
|--------|----------|-------------|------|
| GET | `/api/utilisateurs` | List all users | ADMIN |
| GET | `/api/utilisateurs/{id}` | Get user by ID | ADMIN |
| POST | `/api/utilisateurs` | Create user | ADMIN |
| PUT | `/api/utilisateurs/{id}` | Update user | ADMIN |
| DELETE | `/api/utilisateurs/{id}` | Delete user | ADMIN |

### Dashboard

| Method | Endpoint | Description | Role |
|--------|----------|-------------|------|
| GET | `/api/dashboard/stats` | Aggregated statistics | QUALITE, ADMIN |

### History

| Method | Endpoint | Description | Role |
|--------|----------|-------------|------|
| GET | `/api/historique/retour/{retourId}` | History for a return | Any |
| GET | `/api/historique/recent` | Recent activity feed | Any |

## Default Credentials

Change these before deploying to production.

| Role | Email | Password |
|------|-------|----------|
| ADMIN | admin@retours.com | Admin123! |
| QUALITE | qualite@retours.com | Qualite123! |
| EMPLOYE | employe@retours.com | Employe123! |

## Related Repository

Frontend: [RaturnManagementSystem_Frontend](https://github.com/syrinemrf/RaturnManagementSystem_Frontend) - Live at [gestion-retours-frontend-176480887870.europe-west1.run.app](https://gestion-retours-frontend-176480887870.europe-west1.run.app)

## License

MIT License
