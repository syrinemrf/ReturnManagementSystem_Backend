package com.gestionretours.backend.service;

import com.gestionretours.backend.model.dto.request.RegisterRequest;
import com.gestionretours.backend.model.dto.response.UtilisateurResponse;

import java.util.List;

/**
 * User service interface / Interface du service utilisateur
 */
public interface UtilisateurService {

    List<UtilisateurResponse> findAll();

    UtilisateurResponse findById(Long id);

    UtilisateurResponse create(RegisterRequest request);

    UtilisateurResponse update(Long id, RegisterRequest request);

    void delete(Long id);
}
