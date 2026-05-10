package com.gestionretours.backend.service;

import com.gestionretours.backend.converter.NonConformiteConverter;
import com.gestionretours.backend.model.dto.request.NonConformiteRequest;
import com.gestionretours.backend.model.dto.response.NonConformiteResponse;
import com.gestionretours.backend.model.entity.NonConformite;
import com.gestionretours.backend.model.entity.RetourProduit;
import com.gestionretours.backend.repository.NonConformiteRepository;
import com.gestionretours.backend.repository.RetourProduitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

// Service pour les non-conformités liées aux retours produits
@Service
@RequiredArgsConstructor
public class NonConformiteService {

    private final NonConformiteRepository nonConformiteRepository;
    private final RetourProduitRepository retourProduitRepository;
    private final NonConformiteConverter nonConformiteConverter;

    @Transactional(readOnly = true)
    public List<NonConformiteResponse> findAll() {
        return nonConformiteRepository.findAll()
                .stream()
                .map(nonConformiteConverter::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public NonConformiteResponse findById(Long id) {
        return nonConformiteRepository.findById(id)
                .map(nonConformiteConverter::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Non-conformité introuvable avec l'id: " + id));
    }

    @Transactional(readOnly = true)
    public List<NonConformiteResponse> findByRetourId(Long retourId) {
        return nonConformiteRepository.findByRetour_Id(retourId)
                .stream()
                .map(nonConformiteConverter::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public NonConformiteResponse create(NonConformiteRequest request) {
        // Le retour associé est optionnel, mais en pratique on devrait toujours en avoir un
        RetourProduit retour = null;
        if (request.getRetourId() != null) {
            retour = retourProduitRepository.findById(request.getRetourId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Retour introuvable avec l'id: " + request.getRetourId()));
        }

        NonConformite nc = nonConformiteConverter.toEntity(request);
        nc.setRetour(retour);
        nc = nonConformiteRepository.save(nc);
        return nonConformiteConverter.toDto(nc);
    }

    @Transactional
    public NonConformiteResponse update(Long id, NonConformiteRequest request) {
        NonConformite nc = nonConformiteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Non-conformité introuvable avec l'id: " + id));

        nonConformiteConverter.updateEntityFromRequest(request, nc);

        if (request.getRetourId() != null) {
            RetourProduit retour = retourProduitRepository.findById(request.getRetourId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Retour introuvable avec l'id: " + request.getRetourId()));
            nc.setRetour(retour);
        }

        nc = nonConformiteRepository.save(nc);
        return nonConformiteConverter.toDto(nc);
    }

    @Transactional
    public void delete(Long id) {
        nonConformiteRepository.findById(id).ifPresentOrElse(
                nc -> nonConformiteRepository.deleteById(id),
                () -> { throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Non-conformité introuvable avec l'id: " + id); }
        );
    }
}
