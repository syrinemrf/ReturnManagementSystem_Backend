# Système de Gestion des Retours — Backend API
# Product Return Management System — Backend API

> Production-grade REST API built with Spring Boot 3 for managing product returns, non-conformities, and quality tracking.  
> API REST de qualité production construite avec Spring Boot 3 pour la gestion des retours produits, non-conformités et suivi qualité.

---

## Description

This backend provides a complete REST API for a **Product Return Management System** (Système de Gestion des Retours Produits).  
It handles:
- 🔄 Product return lifecycle management (EN_ATTENTE → EN_COURS → VALIDE / REJETE / TRAITE)
- ⚠️ Non-conformity tracking with severity levels
- 📋 Full audit trail / historique complet
- 📊 Dashboard statistics
- 🔐 JWT-based authentication with role-based access control (RBAC)

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

## Architecture

```
HTTP Request
     │
     ▼
┌─────────────────────────────┐
│       JwtAuthFilter         │  ← Validates Bearer token on every request
└─────────────┬───────────────┘
              │
     ▼
┌─────────────────────────────┐
│        Controller Layer     │  ← REST endpoints, input validation (@Valid)
│  AuthController             │
│  RetourProduitController    │
│  NonConformiteController    │
│  UtilisateurController      │
│  DashboardController        │
│  HistoriqueRetourController │
└─────────────┬───────────────┘
              │
     ▼
┌─────────────────────────────┐
│        Service Layer        │  ← Business logic, transactions
│  AuthServiceImpl            │
│  RetourProduitServiceImpl   │
│  NonConformiteServiceImpl   │
│  UtilisateurServiceImpl     │
│  DashboardServiceImpl       │
│  HistoriqueRetourServiceImpl│
└─────────────┬───────────────┘
              │
     ▼
┌─────────────────────────────┐
│      Repository Layer       │  ← Spring Data JPA
│  UtilisateurRepository      │
│  RetourProduitRepository    │
│  NonConformiteRepository    │
│  HistoriqueRetourRepository │
└─────────────┬───────────────┘
              │
     ▼
┌─────────────────────────────┐
│   PostgreSQL (Neon Cloud)   │
└─────────────────────────────┘
```

---

## Project Structure

```
src/main/java/com/gestionretours/backend/
├── BackendApplication.java
├── config/
│   ├── CorsConfig.java
│   ├── DataInitializer.java
│   ├── EntityMapper.java
│   ├── SecurityConfig.java
│   └── SwaggerConfig.java
├── controller/
│   ├── AuthController.java
│   ├── DashboardController.java
│   ├── HistoriqueRetourController.java
│   ├── NonConformiteController.java
│   ├── RetourProduitController.java
│   └── UtilisateurController.java
├── exception/
│   ├── BadRequestException.java
│   ├── GlobalExceptionHandler.java
│   ├── ResourceNotFoundException.java
│   └── UnauthorizedException.java
├── model/
│   ├── dto/
│   │   ├── request/
│   │   │   ├── ChangerEtatRequest.java
│   │   │   ├── LoginRequest.java
│   │   │   ├── NonConformiteRequest.java
│   │   │   ├── RegisterRequest.java
│   │   │   └── RetourRequest.java
│   │   └── response/
│   │       ├── ApiErrorResponse.java
│   │       ├── AuthResponse.java
│   │       ├── DashboardStatsResponse.java
│   │       ├── HistoriqueRetourResponse.java
│   │       ├── NonConformiteResponse.java
│   │       ├── RetourProduitResponse.java
│   │       └── UtilisateurResponse.java
│   ├── entity/
│   │   ├── HistoriqueRetour.java
│   │   ├── NonConformite.java
│   │   ├── RetourProduit.java
│   │   └── Utilisateur.java
│   └── enums/
│       ├── EtatTraitement.java
│       ├── Gravite.java
│       └── RoleUtilisateur.java
├── repository/
│   ├── HistoriqueRetourRepository.java
│   ├── NonConformiteRepository.java
│   ├── RetourProduitRepository.java
│   └── UtilisateurRepository.java
├── security/
│   ├── JwtAuthFilter.java
│   ├── JwtUtils.java
│   └── UserDetailsServiceImpl.java
└── service/
    ├── AuthService.java
    ├── DashboardService.java
    ├── HistoriqueRetourService.java
    ├── NonConformiteService.java
    ├── RetourProduitService.java
    ├── UtilisateurService.java
    └── impl/
        ├── AuthServiceImpl.java
        ├── DashboardServiceImpl.java
        ├── HistoriqueRetourServiceImpl.java
        ├── NonConformiteServiceImpl.java
        ├── RetourProduitServiceImpl.java
        └── UtilisateurServiceImpl.java
```

---

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.9+ (or use the included `mvnw` wrapper)
- Git

### Installation & Run Locally

```bash
# Clone the repository
git clone https://github.com/YOUR_USERNAME/ReturnManagementSystem.git
cd ReturnManagementSystem/backend

# Run with Maven wrapper
./mvnw spring-boot:run          # Linux/Mac
mvnw.cmd spring-boot:run        # Windows
```

The API will start at: `http://localhost:8080`

### Run with Docker

```bash
# Build and start
docker-compose up --build

# Run in background
docker-compose up -d --build

# Stop
docker-compose down
```

---

## API Endpoints

### 🔐 Authentication — `/api/auth`

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/auth/login` | Login and get JWT token / Connexion | ❌ Public |
| POST | `/api/auth/register` | Register new user / Inscription | ❌ Public |

### 📦 Product Returns — `/api/retours`

| Method | Endpoint | Description | Role Required |
|--------|----------|-------------|---------------|
| GET | `/api/retours` | Get all returns / Tous les retours | 🔑 Any authenticated |
| GET | `/api/retours/{id}` | Get return by ID | 🔑 Any authenticated |
| GET | `/api/retours/etat/{etat}` | Filter by state / Filtrer par état | 🔑 Any authenticated |
| POST | `/api/retours` | Create return / Créer un retour | 🟡 EMPLOYE, QUALITE, ADMIN |
| PUT | `/api/retours/{id}` | Update return / Modifier un retour | 🟠 QUALITE, ADMIN |
| PUT | `/api/retours/{id}/etat` | Change state / Changer l'état | 🟠 QUALITE, ADMIN |
| DELETE | `/api/retours/{id}` | Delete return / Supprimer | 🔴 ADMIN only |

### ⚠️ Non-Conformities — `/api/non-conformites`

| Method | Endpoint | Description | Role Required |
|--------|----------|-------------|---------------|
| GET | `/api/non-conformites` | Get all / Toutes | 🔑 Any authenticated |
| GET | `/api/non-conformites/{id}` | Get by ID | 🔑 Any authenticated |
| GET | `/api/non-conformites/retour/{retourId}` | By return ID / Par retour | 🔑 Any authenticated |
| POST | `/api/non-conformites` | Create / Créer | 🟠 QUALITE, ADMIN |
| PUT | `/api/non-conformites/{id}` | Update / Modifier | 🟠 QUALITE, ADMIN |
| DELETE | `/api/non-conformites/{id}` | Delete / Supprimer | 🔴 ADMIN only |

### 👥 Users — `/api/utilisateurs`

| Method | Endpoint | Description | Role Required |
|--------|----------|-------------|---------------|
| GET | `/api/utilisateurs` | Get all users / Tous les utilisateurs | 🔴 ADMIN only |
| GET | `/api/utilisateurs/{id}` | Get user by ID | 🔴 ADMIN only |
| POST | `/api/utilisateurs` | Create user / Créer | 🔴 ADMIN only |
| PUT | `/api/utilisateurs/{id}` | Update user / Modifier | 🔴 ADMIN only |
| DELETE | `/api/utilisateurs/{id}` | Delete user / Supprimer | 🔴 ADMIN only |

### 📊 Dashboard — `/api/dashboard`

| Method | Endpoint | Description | Role Required |
|--------|----------|-------------|---------------|
| GET | `/api/dashboard/stats` | Dashboard statistics / Statistiques | 🟠 QUALITE, ADMIN |

### 📋 History — `/api/historique`

| Method | Endpoint | Description | Role Required |
|--------|----------|-------------|---------------|
| GET | `/api/historique/retour/{retourId}` | History by return / Historique par retour | 🔑 Any authenticated |
| GET | `/api/historique/recent` | Recent activity / Activité récente | 🔑 Any authenticated |

---

## Default Credentials

> ⚠️ Change these in production / Changez-les en production

| Role | Email | Password |
|------|-------|----------|
| ADMIN | admin@retours.com | Admin123! |
| QUALITE | qualite@retours.com | Qualite123! |
| EMPLOYE | employe@retours.com | Employe123! |

---

## Swagger UI

Once the application is running, access the interactive API documentation at:

**URL:** `http://localhost:8080/swagger-ui.html`

### How to authenticate in Swagger:
1. Call `POST /api/auth/login` with your credentials
2. Copy the `token` value from the response
3. Click the **Authorize 🔒** button at the top right
4. Enter: `Bearer <your_token>` in the value field
5. Click **Authorize** → all secured endpoints are now accessible

---

## Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `SPRING_DATASOURCE_URL` | PostgreSQL connection URL | Neon cloud URL |
| `SPRING_DATASOURCE_USERNAME` | DB username | neondb_owner |
| `SPRING_DATASOURCE_PASSWORD` | DB password | (set in properties) |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | Schema strategy | update |
| `APP_JWT_SECRET` | JWT signing secret | (set in properties) |
| `APP_JWT_EXPIRATION` | JWT expiry in ms | 86400000 (24h) |
| `SERVER_PORT` | Application port | 8080 |

---

## Git Commit Convention

This project follows the **Conventional Commits** specification:

| Prefix | Usage |
|--------|-------|
| `feat:` | New feature / Nouvelle fonctionnalité |
| `chore:` | Build config, tooling / Configuration, outils |
| `fix:` | Bug fix / Correction de bug |
| `docs:` | Documentation only / Documentation uniquement |
| `refactor:` | Code refactor / Refactorisation |
| `test:` | Test addition / Ajout de tests |

---

## GitHub Repository Structure

```
ReturnManagementSystem/
├── backend/        ← This Spring Boot API
└── frontend/       ← Angular frontend (coming soon)
```

---

## License

MIT License — see [LICENSE](LICENSE) for details.
