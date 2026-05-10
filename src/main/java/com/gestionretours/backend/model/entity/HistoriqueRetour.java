package com.gestionretours.backend.model.entity;

import com.gestionretours.backend.model.enums.EtatTraitement;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// Garde une trace de chaque changement d'état sur un retour — utile pour l'audit
@Entity
@Table(name = "historique_retours")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoriqueRetour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "retour_id", nullable = false)
    private RetourProduit retour;

    @NotBlank
    @Column(nullable = false)
    private String action;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employe_id")
    private Utilisateur employe;

    private LocalDateTime date = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private EtatTraitement ancienEtat;

    @Enumerated(EnumType.STRING)
    private EtatTraitement nouvelEtat;

    @PrePersist
    protected void onCreate() {
        // On force la date si elle n'a pas été initialisée avant l'insertion
        if (date == null) {
            date = LocalDateTime.now();
        }
    }
}
