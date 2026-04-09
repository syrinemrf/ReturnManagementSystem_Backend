package com.gestionretours.backend.service;

import com.gestionretours.backend.model.dto.request.LoginRequest;
import com.gestionretours.backend.model.dto.request.RegisterRequest;
import com.gestionretours.backend.model.dto.response.AuthResponse;
import com.gestionretours.backend.model.dto.response.UtilisateurResponse;

/**
 * Authentication service interface / Interface du service d'authentification
 */
public interface AuthService {

    AuthResponse login(LoginRequest request);

    UtilisateurResponse register(RegisterRequest request);
}
