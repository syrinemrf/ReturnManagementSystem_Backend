package com.gestionretours.backend.model.dto.response;

import com.gestionretours.backend.model.enums.RoleUtilisateur;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * User response DTO / DTO de réponse utilisateur
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UtilisateurResponse {

    private Long id;
    private String nom;
    private String email;
    private RoleUtilisateur role;
    private LocalDateTime createdAt;
}
