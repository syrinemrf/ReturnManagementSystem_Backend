package com.gestionretours.backend.service.impl;

import com.gestionretours.backend.config.EntityMapper;
import com.gestionretours.backend.exception.BadRequestException;
import com.gestionretours.backend.exception.ResourceNotFoundException;
import com.gestionretours.backend.model.dto.request.RegisterRequest;
import com.gestionretours.backend.model.dto.response.UtilisateurResponse;
import com.gestionretours.backend.model.entity.Utilisateur;
import com.gestionretours.backend.model.enums.RoleUtilisateur;
import com.gestionretours.backend.repository.UtilisateurRepository;
import com.gestionretours.backend.service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * User service implementation / Implémentation du service utilisateur
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityMapper entityMapper;

    @Override
    @Transactional(readOnly = true)
    public List<UtilisateurResponse> findAll() {
        return utilisateurRepository.findAll()
                .stream()
                .map(entityMapper::toUtilisateurResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UtilisateurResponse findById(Long id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", id));
        return entityMapper.toUtilisateurResponse(utilisateur);
    }

    @Override
    @Transactional
    public UtilisateurResponse create(RegisterRequest request) {
        if (utilisateurRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException(
                    "Email already in use / Email déjà utilisé: " + request.getEmail());
        }

        Utilisateur utilisateur = Utilisateur.builder()
                .nom(request.getNom())
                .email(request.getEmail())
                .motDePasse(passwordEncoder.encode(request.getMotDePasse()))
                .role(request.getRole() != null ? request.getRole() : RoleUtilisateur.ROLE_EMPLOYE)
                .build();

        utilisateur = utilisateurRepository.save(utilisateur);
        log.debug("Created Utilisateur with id: {}", utilisateur.getId());
        return entityMapper.toUtilisateurResponse(utilisateur);
    }

    @Override
    @Transactional
    public UtilisateurResponse update(Long id, RegisterRequest request) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", id));

        // Check email uniqueness if changed / Vérifier unicité email si modifié
        if (!utilisateur.getEmail().equals(request.getEmail())
                && utilisateurRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException(
                    "Email already in use / Email déjà utilisé: " + request.getEmail());
        }

        utilisateur.setNom(request.getNom());
        utilisateur.setEmail(request.getEmail());
        if (request.getMotDePasse() != null && !request.getMotDePasse().isBlank()) {
            utilisateur.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));
        }
        if (request.getRole() != null) {
            utilisateur.setRole(request.getRole());
        }

        utilisateur = utilisateurRepository.save(utilisateur);
        return entityMapper.toUtilisateurResponse(utilisateur);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!utilisateurRepository.existsById(id)) {
            throw new ResourceNotFoundException("Utilisateur", "id", id);
        }
        utilisateurRepository.deleteById(id);
        log.debug("Deleted Utilisateur with id: {}", id);
    }
}
