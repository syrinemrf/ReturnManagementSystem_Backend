package com.gestionretours.backend.model.dto.response;

import com.gestionretours.backend.model.enums.Gravite;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// Données retournées pour une non-conformité — inclut le lien vers le retour concerné
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NonConformiteResponse {

    private Long id;
    private String description;
    private Gravite gravite;
    private LocalDateTime date;
    private String produit;
    private Long retourId;
    private String retourProduit;
}
