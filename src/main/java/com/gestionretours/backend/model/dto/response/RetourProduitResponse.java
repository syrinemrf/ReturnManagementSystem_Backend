package com.gestionretours.backend.model.dto.response;

import com.gestionretours.backend.model.enums.EtatTraitement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// Ce que le frontend reçoit quand il consulte un retour produit
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetourProduitResponse {

    private Long id;
    private String produit;
    private String client;
    private String raison;
    private String description;
    private EtatTraitement etatTraitement;
    private LocalDateTime date;
    private LocalDateTime updatedAt;
    private String utilisateurNom;
    private Long utilisateurId;
}
