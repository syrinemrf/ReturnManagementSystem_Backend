package com.gestionretours.backend.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Ce que le client envoie pour se connecter — juste email + mot de passe
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Email is required / L'email est requis")
    @Email(message = "Email must be valid / L'email doit être valide")
    private String email;

    @NotBlank(message = "Password is required / Le mot de passe est requis")
    private String motDePasse;
}
