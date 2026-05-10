package com.gestionretours.backend.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// Toutes les stats retournées pour le tableau de bord en une seule requête
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsResponse {

    private Long totalRetours;
    private Long retoursEnAttente;
    private Long retoursEnCours;
    private Long retoursValides;
    private Long retoursTraites;
    private Long retoursRejetes;
    private Long totalNonConformites;
    private Long nonConformitesCritiques;
    private Long nonConformitesHautes;
    private Double tauxResolution;
    private List<RetourProduitResponse> recentRetours;
    private List<HistoriqueRetourResponse> recentActivite;
}
