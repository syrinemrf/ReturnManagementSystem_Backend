package com.gestionretours.backend.service.impl;

import com.gestionretours.backend.config.EntityMapper;
import com.gestionretours.backend.exception.ResourceNotFoundException;
import com.gestionretours.backend.model.dto.response.HistoriqueRetourResponse;
import com.gestionretours.backend.repository.HistoriqueRetourRepository;
import com.gestionretours.backend.repository.RetourProduitRepository;
import com.gestionretours.backend.service.HistoriqueRetourService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Return history service implementation / Implémentation du service historique retour
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class HistoriqueRetourServiceImpl implements HistoriqueRetourService {

    private final HistoriqueRetourRepository historiqueRetourRepository;
    private final RetourProduitRepository retourProduitRepository;
    private final EntityMapper entityMapper;

    @Override
    @Transactional(readOnly = true)
    public List<HistoriqueRetourResponse> findByRetourId(Long retourId) {
        if (!retourProduitRepository.existsById(retourId)) {
            throw new ResourceNotFoundException("RetourProduit", "id", retourId);
        }
        return historiqueRetourRepository.findByRetour_IdOrderByDateDesc(retourId)
                .stream()
                .map(entityMapper::toHistoriqueResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistoriqueRetourResponse> findRecent() {
        return historiqueRetourRepository.findTop10ByOrderByDateDesc()
                .stream()
                .map(entityMapper::toHistoriqueResponse)
                .collect(Collectors.toList());
    }
}
