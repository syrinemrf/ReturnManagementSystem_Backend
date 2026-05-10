package com.gestionretours.backend.service;

import com.gestionretours.backend.converter.UtilisateurConverter;
import com.gestionretours.backend.model.dto.request.LoginRequest;
import com.gestionretours.backend.model.dto.request.RegisterRequest;
import com.gestionretours.backend.model.dto.response.AuthResponse;
import com.gestionretours.backend.model.dto.response.UtilisateurResponse;
import com.gestionretours.backend.model.entity.Utilisateur;
import com.gestionretours.backend.model.enums.RoleUtilisateur;
import com.gestionretours.backend.repository.UtilisateurRepository;
import com.gestionretours.backend.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

// Service qui gère la connexion et l'inscription des utilisateurs
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final UtilisateurConverter utilisateurConverter;

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        // On cherche l'utilisateur par email — si introuvable, on retourne 401 directement
        Utilisateur utilisateur = utilisateurRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email ou mot de passe invalide"));

        // Vérification du mot de passe avec BCrypt
        if (!passwordEncoder.matches(request.getMotDePasse(), utilisateur.getMotDePasse())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email ou mot de passe invalide");
        }

        // Tout est OK, on génère le token JWT
        String token = jwtUtils.generateToken(utilisateur.getEmail(), utilisateur.getRole().name());

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setType("Bearer");
        response.setNom(utilisateur.getNom());
        response.setEmail(utilisateur.getEmail());
        response.setRole(utilisateur.getRole().name());
        return response;
    }

    @Transactional
    public UtilisateurResponse register(RegisterRequest request) {
        // On vérifie d'abord que l'email n'est pas déjà pris
        if (utilisateurRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email déjà utilisé: " + request.getEmail());
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(request.getNom());
        utilisateur.setEmail(request.getEmail());
        utilisateur.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));
        utilisateur.setRole(request.getRole() != null ? request.getRole() : RoleUtilisateur.ROLE_EMPLOYE);

        utilisateur = utilisateurRepository.save(utilisateur);
        return utilisateurConverter.toDto(utilisateur);
    }
}
