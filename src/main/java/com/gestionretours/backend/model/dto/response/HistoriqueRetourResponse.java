package com.gestionretours.backend.model.dto.response;

import com.gestionretours.backend.model.enums.EtatTraitement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// Représente une ligne dans l'historique des changements d'état d'un retour
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoriqueRetourResponse {

    private Long id;
    private String action;
    private LocalDateTime date;
    private EtatTraitement ancienEtat;
    private EtatTraitement nouvelEtat;
    private Long retourId;
    private String employeNom;
}
