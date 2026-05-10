package com.gestionretours.backend.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Données envoyées par le client pour créer ou modifier un retour produit
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetourRequest {

    @NotBlank(message = "Product is required / Le produit est requis")
    private String produit;

    @NotBlank(message = "Client is required / Le client est requis")
    private String client;

    @NotBlank(message = "Reason is required / La raison est requise")
    private String raison;

    private String description;

    private Long utilisateurId;
}
