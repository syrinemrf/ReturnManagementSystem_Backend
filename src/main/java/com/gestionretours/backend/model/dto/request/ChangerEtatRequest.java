package com.gestionretours.backend.model.dto.request;

import com.gestionretours.backend.model.enums.EtatTraitement;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Utilisé pour changer l'état d'un retour — on peut aussi ajouter un commentaire optionnel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangerEtatRequest {

    @NotNull(message = "New state is required / Le nouvel état est requis")
    private EtatTraitement nouvelEtat;

    @NotNull(message = "Employee ID is required / L'ID employé est requis")
    private Long employeId;

    private String commentaire;
}
