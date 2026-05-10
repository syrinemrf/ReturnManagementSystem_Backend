package com.gestionretours.backend.model.entity;

import com.gestionretours.backend.model.enums.EtatTraitement;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// Entité principale du système — représente un retour de produit déposé par un client
@Entity
@Table(name = "retours_produit")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetourProduit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String produit;

    @NotBlank
    @Column(nullable = false)
    private String client;

    @NotBlank
    @Column(nullable = false)
    private String raison;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EtatTraitement etatTraitement = EtatTraitement.EN_ATTENTE;

    @Column(updatable = false)
    private LocalDateTime date = LocalDateTime.now();

    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @PrePersist
    protected void onCreate() {
        // Initialisation des valeurs par défaut si elles ne sont pas déjà renseignées
        if (date == null) {
            date = LocalDateTime.now();
        }
        if (etatTraitement == null) {
            etatTraitement = EtatTraitement.EN_ATTENTE;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        // On met à jour la date de modification à chaque changement sur l'entité
        updatedAt = LocalDateTime.now();
    }
}
