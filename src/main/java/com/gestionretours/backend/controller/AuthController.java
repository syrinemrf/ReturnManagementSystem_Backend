package com.gestionretours.backend.controller;

import com.gestionretours.backend.model.dto.request.LoginRequest;
import com.gestionretours.backend.model.dto.request.RegisterRequest;
import com.gestionretours.backend.model.dto.response.AuthResponse;
import com.gestionretours.backend.model.dto.response.UtilisateurResponse;
import com.gestionretours.backend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication controller / Contrôleur d'authentification
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication endpoints / Points de terminaison d'authentification")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "User login / Connexion utilisateur",
            description = "Authenticate with email and password to get JWT token / Authentifiez-vous avec email et mot de passe pour obtenir un token JWT",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login successful / Connexion réussie"),
                    @ApiResponse(responseCode = "401", description = "Invalid credentials / Identifiants invalides")
            })
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    @Operation(summary = "Register new user / Inscrire un nouvel utilisateur",
            description = "Create a new user account / Créer un nouveau compte utilisateur",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User created / Utilisateur créé"),
                    @ApiResponse(responseCode = "400", description = "Email already in use / Email déjà utilisé")
            })
    public ResponseEntity<UtilisateurResponse> register(@Valid @RequestBody RegisterRequest request) {
        return new ResponseEntity<>(authService.register(request), HttpStatus.CREATED);
    }
}
