package com.gestionretours.backend.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Ce que le serveur renvoie après une connexion réussie — contient le token JWT et les infos de base
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
