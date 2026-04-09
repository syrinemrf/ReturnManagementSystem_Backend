package com.gestionretours.backend.model.dto.request;

import com.gestionretours.backend.model.enums.Gravite;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Non-conformity request DTO / DTO de demande de non-conformité
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NonConformiteRequest {

    @NotBlank(message = "Description is required / La description est requise")
    private String description;

    private Gravite gravite = Gravite.MOYENNE;

    @NotBlank(message = "Product is required / Le produit est requis")
    private String produit;

    private Long retourId;
}
