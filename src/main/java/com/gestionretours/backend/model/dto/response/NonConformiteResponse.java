package com.gestionretours.backend.model.dto.response;

import com.gestionretours.backend.model.enums.Gravite;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Non-conformity response DTO / DTO de réponse non-conformité
 */
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
