package com.gestionretours.backend.service.impl;

import com.gestionretours.backend.config.EntityMapper;
import com.gestionretours.backend.model.dto.response.DashboardStatsResponse;
import com.gestionretours.backend.model.dto.response.HistoriqueRetourResponse;
import com.gestionretours.backend.model.dto.response.RetourProduitResponse;
import com.gestionretours.backend.model.enums.EtatTraitement;
import com.gestionretours.backend.model.enums.Gravite;
import com.gestionretours.backend.repository.HistoriqueRetourRepository;
import com.gestionretours.backend.repository.NonConformiteRepository;
import com.gestionretours.backend.repository.RetourProduitRepository;
import com.gestionretours.backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Dashboard service implementation / Implémentation du service tableau de bord
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardServiceImpl implements DashboardService {

    private final RetourProduitRepository retourProduitRepository;
    private final NonConformiteRepository nonConformiteRepository;
    private final HistoriqueRetourRepository historiqueRetourRepository;
    private final EntityMapper entityMapper;

    @Override
    @Transactional(readOnly = true)
    public DashboardStatsResponse getStats() {
        long total = retourProduitRepository.count();
        long enAttente = retourProduitRepository.countByEtatTraitement(EtatTraitement.EN_ATTENTE);
        long enCours = retourProduitRepository.countByEtatTraitement(EtatTraitement.EN_COURS);
        long valides = retourProduitRepository.countByEtatTraitement(EtatTraitement.VALIDE);
        long traites = retourProduitRepository.countByEtatTraitement(EtatTraitement.TRAITE);
        long rejetes = retourProduitRepository.countByEtatTraitement(EtatTraitement.REJETE);

        long totalNc = nonConformiteRepository.count();
        long critiques = nonConformiteRepository.countByGravite(Gravite.CRITIQUE);
        long hautes = nonConformiteRepository.countByGravite(Gravite.HAUTE);

        double tauxResolution = total > 0
                ? BigDecimal.valueOf((double) traites / total * 100)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue()
                : 0.0;

        List<RetourProduitResponse> recentRetours = retourProduitRepository
                .findTop5ByOrderByDateDesc()
                .stream()
                .map(entityMapper::toRetourResponse)
                .collect(Collectors.toList());

        List<HistoriqueRetourResponse> recentActivite = historiqueRetourRepository
                .findTop10ByOrderByDateDesc()
                .stream()
                .map(entityMapper::toHistoriqueResponse)
                .collect(Collectors.toList());

        return DashboardStatsResponse.builder()
                .totalRetours(total)
                .retoursEnAttente(enAttente)
                .retoursEnCours(enCours)
                .retoursValides(valides)
                .retoursTraites(traites)
                .retoursRejetes(rejetes)
                .totalNonConformites(totalNc)
                .nonConformitesCritiques(critiques)
                .nonConformitesHautes(hautes)
                .tauxResolution(tauxResolution)
                .recentRetours(recentRetours)
                .recentActivite(recentActivite)
                .build();
    }
}
