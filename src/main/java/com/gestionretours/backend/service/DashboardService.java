package com.gestionretours.backend.service;

import com.gestionretours.backend.converter.HistoriqueRetourConverter;
import com.gestionretours.backend.converter.RetourProduitConverter;
import com.gestionretours.backend.model.dto.response.DashboardStatsResponse;
import com.gestionretours.backend.model.dto.response.HistoriqueRetourResponse;
import com.gestionretours.backend.model.dto.response.RetourProduitResponse;
import com.gestionretours.backend.model.enums.EtatTraitement;
import com.gestionretours.backend.model.enums.Gravite;
import com.gestionretours.backend.repository.HistoriqueRetourRepository;
import com.gestionretours.backend.repository.NonConformiteRepository;
import com.gestionretours.backend.repository.RetourProduitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

// Ce service agrège toutes les stats pour le dashboard
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final RetourProduitRepository retourProduitRepository;
    private final NonConformiteRepository nonConformiteRepository;
    private final HistoriqueRetourRepository historiqueRetourRepository;
    private final RetourProduitConverter retourProduitConverter;
    private final HistoriqueRetourConverter historiqueRetourConverter;

    @Transactional(readOnly = true)
    public DashboardStatsResponse getStats() {
        // Comptage des retours par état
        long total = retourProduitRepository.count();
        long enAttente = retourProduitRepository.countByEtatTraitement(EtatTraitement.EN_ATTENTE);
        long enCours = retourProduitRepository.countByEtatTraitement(EtatTraitement.EN_COURS);
        long valides = retourProduitRepository.countByEtatTraitement(EtatTraitement.VALIDE);
        long traites = retourProduitRepository.countByEtatTraitement(EtatTraitement.TRAITE);
        long rejetes = retourProduitRepository.countByEtatTraitement(EtatTraitement.REJETE);

        // Comptage des non-conformités par gravité
        long totalNc = nonConformiteRepository.count();
        long critiques = nonConformiteRepository.countByGravite(Gravite.CRITIQUE);
        long hautes = nonConformiteRepository.countByGravite(Gravite.HAUTE);

        // Taux de résolution = retours traités / total (arrondi à 2 décimales)
        double tauxResolution = total > 0
                ? BigDecimal.valueOf((double) traites / total * 100)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue()
                : 0.0;

        List<RetourProduitResponse> recentRetours = retourProduitRepository
                .findTop5ByOrderByDateDesc()
                .stream()
                .map(retourProduitConverter::toDto)
                .collect(Collectors.toList());

        List<HistoriqueRetourResponse> recentActivite = historiqueRetourRepository
                .findTop10ByOrderByDateDesc()
                .stream()
                .map(historiqueRetourConverter::toDto)
                .collect(Collectors.toList());

        DashboardStatsResponse stats = new DashboardStatsResponse();
        stats.setTotalRetours(total);
        stats.setRetoursEnAttente(enAttente);
        stats.setRetoursEnCours(enCours);
        stats.setRetoursValides(valides);
        stats.setRetoursTraites(traites);
        stats.setRetoursRejetes(rejetes);
        stats.setTotalNonConformites(totalNc);
        stats.setNonConformitesCritiques(critiques);
        stats.setNonConformitesHautes(hautes);
        stats.setTauxResolution(tauxResolution);
        stats.setRecentRetours(recentRetours);
        stats.setRecentActivite(recentActivite);
        return stats;
    }
}
