package com.gestionretours.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gestionretours.backend.model.enums.EtatTraitement;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Return history entity / Entité historique des retours
 */
@Entity
@Table(name = "historique_retours")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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

    @Builder.Default
    private LocalDateTime date = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private EtatTraitement ancienEtat;

    @Enumerated(EnumType.STRING)
    private EtatTraitement nouvelEtat;

    @PrePersist
    protected void onCreate() {
        if (date == null) {
            date = LocalDateTime.now();
        }
    }
}
