@echo off
REM ============================================================
REM run-local.cmd — Start the backend with local .env variables
REM Démarre le backend avec les variables du fichier .env local
REM ============================================================

set SPRING_DATASOURCE_URL=jdbc:postgresql://ep-wandering-pond-amsao736-pooler.c-5.us-east-1.aws.neon.tech/neondb?sslmode=require^&channel_binding=require
set SPRING_DATASOURCE_USERNAME=neondb_owner
set SPRING_DATASOURCE_PASSWORD=npg_fnDR5I4HpyMx
set APP_JWT_SECRET=mySecretKeyForJWTTokenGenerationGestionRetours2024XYZ
set APP_JWT_EXPIRATION=86400000
set SERVER_PORT=8080
set JPA_DDL_AUTO=update
set JPA_SHOW_SQL=true
set LOG_LEVEL=DEBUG
set LOG_LEVEL_SQL=DEBUG

echo Starting Gestion Retours Backend on port 8080...
echo Demarrage du backend Gestion Retours sur le port 8080...
echo.

mvnw.cmd spring-boot:run
