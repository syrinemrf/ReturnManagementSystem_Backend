package com.gestionretours.backend.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Authentication response DTO / DTO de réponse d'authentification
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String token;

    private String type = "Bearer";

    private String nom;
    private String email;
    private String role;
}
