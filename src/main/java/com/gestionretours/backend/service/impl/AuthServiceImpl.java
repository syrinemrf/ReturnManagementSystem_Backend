package com.gestionretours.backend.service.impl;

import com.gestionretours.backend.converter.UtilisateurConverter;
import com.gestionretours.backend.model.dto.request.LoginRequest;
import com.gestionretours.backend.model.dto.request.RegisterRequest;
import com.gestionretours.backend.model.dto.response.AuthResponse;
import com.gestionretours.backend.model.dto.response.UtilisateurResponse;
import com.gestionretours.backend.model.entity.Utilisateur;
import com.gestionretours.backend.model.enums.RoleUtilisateur;
import com.gestionretours.backend.repository.UtilisateurRepository;
import com.gestionretours.backend.security.JwtUtils;
import com.gestionretours.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Authentication service implementation / Implémentation du service d'authentification
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final UtilisateurConverter utilisateurConverter;

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        log.debug("Login attempt for email: {}", request.getEmail());

        Utilisateur utilisateur = utilisateurRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email ou mot de passe invalide"));

        if (!passwordEncoder.matches(request.getMotDePasse(), utilisateur.getMotDePasse())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email ou mot de passe invalide");
        }

        String token = jwtUtils.generateToken(utilisateur.getEmail(), utilisateur.getRole().name());

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setType("Bearer");
        response.setNom(utilisateur.getNom());
        response.setEmail(utilisateur.getEmail());
        response.setRole(utilisateur.getRole().name());
        return response;
    }

    @Override
    @Transactional
    public UtilisateurResponse register(RegisterRequest request) {
        log.debug("Register attempt for email: {}", request.getEmail());

        if (utilisateurRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email déjà utilisé: " + request.getEmail());
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(request.getNom());
        utilisateur.setEmail(request.getEmail());
        utilisateur.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));
        utilisateur.setRole(request.getRole() != null ? request.getRole() : RoleUtilisateur.ROLE_EMPLOYE);

        utilisateur = utilisateurRepository.save(utilisateur);
        log.debug("User registered with id: {}", utilisateur.getId());
        return utilisateurConverter.toDto(utilisateur);
    }
}
