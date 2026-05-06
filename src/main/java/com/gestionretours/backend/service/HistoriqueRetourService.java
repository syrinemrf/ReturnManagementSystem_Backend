package com.gestionretours.backend.service;

import com.gestionretours.backend.converter.HistoriqueRetourConverter;
import com.gestionretours.backend.model.dto.response.HistoriqueRetourResponse;
import com.gestionretours.backend.repository.HistoriqueRetourRepository;
import com.gestionretours.backend.repository.RetourProduitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HistoriqueRetourService {

    private final HistoriqueRetourRepository historiqueRetourRepository;
    private final RetourProduitRepository retourProduitRepository;
    private final HistoriqueRetourConverter historiqueRetourConverter;

    @Transactional(readOnly = true)
    public List<HistoriqueRetourResponse> findByRetourId(Long retourId) {
        if (!retourProduitRepository.existsById(retourId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Retour introuvable avec l'id: " + retourId);
        }
        return historiqueRetourRepository.findByRetour_IdOrderByDateDesc(retourId)
                .stream()
                .map(historiqueRetourConverter::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<HistoriqueRetourResponse> findRecent() {
        return historiqueRetourRepository.findTop10ByOrderByDateDesc()
                .stream()
                .map(historiqueRetourConverter::toDto)
                .collect(Collectors.toList());
    }
}
