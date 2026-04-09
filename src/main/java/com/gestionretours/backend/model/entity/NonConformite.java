package com.gestionretours.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gestionretours.backend.model.enums.Gravite;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Non-conformity entity / Entité non-conformité
 */
@Entity
@Table(name = "non_conformites")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class NonConformite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false)
    private Gravite gravite = Gravite.MOYENNE;

    @Builder.Default
    private LocalDateTime date = LocalDateTime.now();

    @NotBlank
    @Column(nullable = false)
    private String produit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "retour_id")
    private RetourProduit retour;

    @PrePersist
    protected void onCreate() {
        if (date == null) {
            date = LocalDateTime.now();
        }
        if (gravite == null) {
            gravite = Gravite.MOYENNE;
        }
    }
}
