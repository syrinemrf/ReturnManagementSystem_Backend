package com.gestionretours.backend.model.dto.response;

import com.gestionretours.backend.model.enums.RoleUtilisateur;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// Ce qu'on renvoie au frontend quand on parle d'un utilisateur
// Note : le mot de passe n'est jamais inclus ici, c'est voulu
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UtilisateurResponse {

    private Long id;
    private String nom;
    private String email;
    private RoleUtilisateur role;
    private LocalDateTime createdAt;
}
