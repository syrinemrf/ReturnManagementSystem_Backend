package com.gestionretours.backend.service.impl;

import com.gestionretours.backend.config.EntityMapper;
import com.gestionretours.backend.exception.BadRequestException;
import com.gestionretours.backend.exception.UnauthorizedException;
import com.gestionretours.backend.model.dto.request.LoginRequest;
import com.gestionretours.backend.model.dto.request.RegisterRequest;
import com.gestionretours.backend.model.dto.response.AuthResponse;
import com.gestionretours.backend.model.dto.response.UtilisateurResponse;
import com.gestionretours.backend.model.entity.Utilisateur;
import com.gestionretours.backend.repository.UtilisateurRepository;
import com.gestionretours.backend.security.JwtUtils;
import com.gestionretours.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
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
    private final EntityMapper entityMapper;

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        log.debug("Login attempt for email: {}", request.getEmail());

        Utilisateur utilisateur = utilisateurRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException(
                        "Invalid email or password / Email ou mot de passe invalide"));

        if (!passwordEncoder.matches(request.getMotDePasse(), utilisateur.getMotDePasse())) {
            throw new UnauthorizedException(
                    "Invalid email or password / Email ou mot de passe invalide");
        }

        String token = jwtUtils.generateToken(utilisateur.getEmail(), utilisateur.getRole().name());

        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .nom(utilisateur.getNom())
                .email(utilisateur.getEmail())
                .role(utilisateur.getRole().name())
                .build();
    }

    @Override
    @Transactional
    public UtilisateurResponse register(RegisterRequest request) {
        log.debug("Register attempt for email: {}", request.getEmail());

        if (utilisateurRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException(
                    "Email already in use / Email déjà utilisé: " + request.getEmail());
        }

        Utilisateur utilisateur = Utilisateur.builder()
                .nom(request.getNom())
                .email(request.getEmail())
                .motDePasse(passwordEncoder.encode(request.getMotDePasse()))
                .role(request.getRole() != null ? request.getRole() :
                        com.gestionretours.backend.model.enums.RoleUtilisateur.ROLE_EMPLOYE)
                .build();

        utilisateur = utilisateurRepository.save(utilisateur);
        log.debug("User registered with id: {}", utilisateur.getId());
        return entityMapper.toUtilisateurResponse(utilisateur);
    }
}
