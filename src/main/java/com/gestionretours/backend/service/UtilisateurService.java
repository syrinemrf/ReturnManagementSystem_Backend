package com.gestionretours.backend.service;

import com.gestionretours.backend.converter.UtilisateurConverter;
import com.gestionretours.backend.model.dto.request.RegisterRequest;
import com.gestionretours.backend.model.dto.response.UtilisateurResponse;
import com.gestionretours.backend.model.entity.Utilisateur;
import com.gestionretours.backend.model.enums.RoleUtilisateur;
import com.gestionretours.backend.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

// Service CRUD pour les utilisateurs — réservé aux admins
@Service
@RequiredArgsConstructor
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final UtilisateurConverter utilisateurConverter;

    @Transactional(readOnly = true)
    public List<UtilisateurResponse> findAll() {
        return utilisateurRepository.findAll()
                .stream()
                .map(utilisateurConverter::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UtilisateurResponse findById(Long id) {
        return utilisateurRepository.findById(id)
                .map(utilisateurConverter::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable avec l'id: " + id));
    }

    @Transactional
    public UtilisateurResponse create(RegisterRequest request) {
        // Vérification de l'unicité de l'email avant de créer
        if (utilisateurRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email déjà utilisé: " + request.getEmail());
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(request.getNom());
        utilisateur.setEmail(request.getEmail());
        utilisateur.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));
        // Si aucun rôle n'est fourni, on met EMPLOYE par défaut
        utilisateur.setRole(request.getRole() != null ? request.getRole() : RoleUtilisateur.ROLE_EMPLOYE);

        utilisateur = utilisateurRepository.save(utilisateur);
        return utilisateurConverter.toDto(utilisateur);
    }

    @Transactional
    public UtilisateurResponse update(Long id, RegisterRequest request) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable avec l'id: " + id));

        // On vérifie que le nouvel email n'appartient pas à quelqu'un d'autre
        if (!utilisateur.getEmail().equals(request.getEmail())
                && utilisateurRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email déjà utilisé: " + request.getEmail());
        }

        utilisateur.setNom(request.getNom());
        utilisateur.setEmail(request.getEmail());
        // On ne change le mot de passe que s'il est fourni dans la requête
        if (request.getMotDePasse() != null && !request.getMotDePasse().isBlank()) {
            utilisateur.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));
        }
        if (request.getRole() != null) {
            utilisateur.setRole(request.getRole());
        }

        utilisateur = utilisateurRepository.save(utilisateur);
        return utilisateurConverter.toDto(utilisateur);
    }

    @Transactional
    public void delete(Long id) {
        utilisateurRepository.findById(id).ifPresentOrElse(
                u -> utilisateurRepository.deleteById(id),
                () -> { throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable avec l'id: " + id); }
        );
    }
}
