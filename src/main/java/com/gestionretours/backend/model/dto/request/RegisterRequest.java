package com.gestionretours.backend.model.dto.request;

import com.gestionretours.backend.model.enums.RoleUtilisateur;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Registration request DTO / DTO de demande d'inscription
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Name is required / Le nom est requis")
    private String nom;

    @NotBlank(message = "Email is required / L'email est requis")
    @Email(message = "Email must be valid / L'email doit être valide")
    private String email;

    @NotBlank(message = "Password is required / Le mot de passe est requis")
    @Size(min = 6, message = "Password must be at least 6 characters / Le mot de passe doit contenir au moins 6 caractères")
    private String motDePasse;

    private RoleUtilisateur role = RoleUtilisateur.ROLE_EMPLOYE;
}
