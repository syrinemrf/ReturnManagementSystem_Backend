package com.gestionretours.backend.service;

import com.gestionretours.backend.model.dto.response.HistoriqueRetourResponse;

import java.util.List;

/**
 * Return history service interface / Interface du service historique retour
 */
public interface HistoriqueRetourService {

    List<HistoriqueRetourResponse> findByRetourId(Long retourId);

    List<HistoriqueRetourResponse> findRecent();
}
