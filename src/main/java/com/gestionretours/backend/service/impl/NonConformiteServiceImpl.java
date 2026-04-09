package com.gestionretours.backend.service.impl;

import com.gestionretours.backend.config.EntityMapper;
import com.gestionretours.backend.exception.ResourceNotFoundException;
import com.gestionretours.backend.model.dto.request.NonConformiteRequest;
import com.gestionretours.backend.model.dto.response.NonConformiteResponse;
import com.gestionretours.backend.model.entity.NonConformite;
import com.gestionretours.backend.model.entity.RetourProduit;
import com.gestionretours.backend.model.enums.Gravite;
import com.gestionretours.backend.repository.NonConformiteRepository;
import com.gestionretours.backend.repository.RetourProduitRepository;
import com.gestionretours.backend.service.NonConformiteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final EntityMapper entityMapper;

    @Override
    @Transactional(readOnly = true)
    public List<NonConformiteResponse> findAll() {
        return nonConformiteRepository.findAll()
                .stream()
                .map(entityMapper::toNonConformiteResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public NonConformiteResponse findById(Long id) {
        NonConformite nc = nonConformiteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("NonConformite", "id", id));
        return entityMapper.toNonConformiteResponse(nc);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NonConformiteResponse> findByRetourId(Long retourId) {
        return nonConformiteRepository.findByRetour_Id(retourId)
                .stream()
                .map(entityMapper::toNonConformiteResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public NonConformiteResponse create(NonConformiteRequest request) {
        RetourProduit retour = null;
        if (request.getRetourId() != null) {
            retour = retourProduitRepository.findById(request.getRetourId())
                    .orElseThrow(() -> new ResourceNotFoundException("RetourProduit", "id", request.getRetourId()));
        }

        NonConformite nc = NonConformite.builder()
                .description(request.getDescription())
                .gravite(request.getGravite() != null ? request.getGravite() : Gravite.MOYENNE)
                .produit(request.getProduit())
                .retour(retour)
                .build();

        nc = nonConformiteRepository.save(nc);
        log.debug("Created NonConformite with id: {}", nc.getId());
        return entityMapper.toNonConformiteResponse(nc);
    }

    @Override
    @Transactional
    public NonConformiteResponse update(Long id, NonConformiteRequest request) {
        NonConformite nc = nonConformiteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("NonConformite", "id", id));

        nc.setDescription(request.getDescription());
        nc.setProduit(request.getProduit());
        if (request.getGravite() != null) {
            nc.setGravite(request.getGravite());
        }
        if (request.getRetourId() != null) {
            RetourProduit retour = retourProduitRepository.findById(request.getRetourId())
                    .orElseThrow(() -> new ResourceNotFoundException("RetourProduit", "id", request.getRetourId()));
            nc.setRetour(retour);
        }

        nc = nonConformiteRepository.save(nc);
        return entityMapper.toNonConformiteResponse(nc);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!nonConformiteRepository.existsById(id)) {
            throw new ResourceNotFoundException("NonConformite", "id", id);
        }
        nonConformiteRepository.deleteById(id);
        log.debug("Deleted NonConformite with id: {}", id);
    }
}
