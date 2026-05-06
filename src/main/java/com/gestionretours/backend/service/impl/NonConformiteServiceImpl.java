package com.gestionretours.backend.service.impl;

import com.gestionretours.backend.converter.NonConformiteConverter;
import com.gestionretours.backend.model.dto.request.NonConformiteRequest;
import com.gestionretours.backend.model.dto.response.NonConformiteResponse;
import com.gestionretours.backend.model.entity.NonConformite;
import com.gestionretours.backend.model.entity.RetourProduit;
import com.gestionretours.backend.repository.NonConformiteRepository;
import com.gestionretours.backend.repository.RetourProduitRepository;
import com.gestionretours.backend.service.NonConformiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Non-conformity service implementation / Implémentation du service non-conformité
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NonConformiteServiceImpl implements NonConformiteService {

    private final NonConformiteRepository nonConformiteRepository;
    private final RetourProduitRepository retourProduitRepository;
    private final NonConformiteConverter nonConformiteConverter;

    @Override
    @Transactional(readOnly = true)
    public List<NonConformiteResponse> findAll() {
        return nonConformiteRepository.findAll()
                .stream()
                .map(nonConformiteConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public NonConformiteResponse findById(Long id) {
        NonConformite nc = nonConformiteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Non-conformité introuvable avec l'id: " + id));
        return nonConformiteConverter.toDto(nc);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NonConformiteResponse> findByRetourId(Long retourId) {
        return nonConformiteRepository.findByRetour_Id(retourId)
                .stream()
                .map(nonConformiteConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @CacheEvict(value = "dashboardStats", allEntries = true)
    @Transactional
    public NonConformiteResponse create(NonConformiteRequest request) {
        RetourProduit retour = null;
        if (request.getRetourId() != null) {
            retour = retourProduitRepository.findById(request.getRetourId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Retour introuvable avec l'id: " + request.getRetourId()));
        }

        NonConformite nc = nonConformiteConverter.toEntity(request);
        nc.setRetour(retour);
        nc = nonConformiteRepository.save(nc);
        log.debug("Created NonConformite with id: {}", nc.getId());
        return nonConformiteConverter.toDto(nc);
    }

    @Override
    @CacheEvict(value = "dashboardStats", allEntries = true)
    @Transactional
    public NonConformiteResponse update(Long id, NonConformiteRequest request) {
        NonConformite nc = nonConformiteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Non-conformité introuvable avec l'id: " + id));

        nonConformiteConverter.updateEntityFromRequest(request, nc);
        if (request.getRetourId() != null) {
            RetourProduit retour = retourProduitRepository.findById(request.getRetourId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Retour introuvable avec l'id: " + request.getRetourId()));
            nc.setRetour(retour);
        }

        nc = nonConformiteRepository.save(nc);
        return nonConformiteConverter.toDto(nc);
    }

    @Override
    @CacheEvict(value = "dashboardStats", allEntries = true)
    @Transactional
    public void delete(Long id) {
        if (!nonConformiteRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Non-conformité introuvable avec l'id: " + id);
        }
        nonConformiteRepository.deleteById(id);
        log.debug("Deleted NonConformite with id: {}", id);
    }
}
