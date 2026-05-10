package com.gestionretours.backend.model.entity;

import com.gestionretours.backend.model.enums.Gravite;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// Une non-conformité est liée à un retour — elle décrit un problème qualité détecté
@Entity
@Table(name = "non_conformites")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NonConformite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gravite gravite = Gravite.MOYENNE;

    private LocalDateTime date = LocalDateTime.now();

    @NotBlank
    @Column(nullable = false)
    private String produit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "retour_id")
    private RetourProduit retour;

    @PrePersist
    protected void onCreate() {
        // On s'assure que la date et la gravité sont toujours renseignées
        if (date == null) {
            date = LocalDateTime.now();
        }
        if (gravite == null) {
            gravite = Gravite.MOYENNE;
        }
    }
}
