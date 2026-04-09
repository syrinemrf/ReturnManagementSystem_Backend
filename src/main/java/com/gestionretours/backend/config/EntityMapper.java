package com.gestionretours.backend.config;

import com.gestionretours.backend.model.dto.response.*;
import com.gestionretours.backend.model.entity.*;
import org.springframework.stereotype.Component;

/**
 * Utility mapper component converting JPA entities to response DTOs.
 * Composant utilitaire convertissant les entités JPA en DTOs de réponse.
 */
@Component
public class EntityMapper {

    public RetourProduitResponse toRetourResponse(RetourProduit r) {
        if (r == null) return null;
        return RetourProduitResponse.builder()
                .id(r.getId())
                .produit(r.getProduit())
                .client(r.getClient())
                .raison(r.getRaison())
                .description(r.getDescription())
                .etatTraitement(r.getEtatTraitement())
                .date(r.getDate())
                .updatedAt(r.getUpdatedAt())
                .utilisateurNom(r.getUtilisateur() != null ? r.getUtilisateur().getNom() : null)
                .utilisateurId(r.getUtilisateur() != null ? r.getUtilisateur().getId() : null)
                .build();
    }

    public NonConformiteResponse toNonConformiteResponse(NonConformite nc) {
        if (nc == null) return null;
        return NonConformiteResponse.builder()
                .id(nc.getId())
                .description(nc.getDescription())
                .gravite(nc.getGravite())
                .date(nc.getDate())
                .produit(nc.getProduit())
                .retourId(nc.getRetour() != null ? nc.getRetour().getId() : null)
                .retourProduit(nc.getRetour() != null ? nc.getRetour().getProduit() : null)
                .build();
    }

    public HistoriqueRetourResponse toHistoriqueResponse(HistoriqueRetour h) {
        if (h == null) return null;
        return HistoriqueRetourResponse.builder()
                .id(h.getId())
                .action(h.getAction())
                .date(h.getDate())
                .ancienEtat(h.getAncienEtat())
                .nouvelEtat(h.getNouvelEtat())
                .retourId(h.getRetour() != null ? h.getRetour().getId() : null)
                .employeNom(h.getEmploye() != null ? h.getEmploye().getNom() : null)
                .build();
    }

    public UtilisateurResponse toUtilisateurResponse(Utilisateur u) {
        if (u == null) return null;
        return UtilisateurResponse.builder()
                .id(u.getId())
                .nom(u.getNom())
                .email(u.getEmail())
                .role(u.getRole())
                .createdAt(u.getCreatedAt())
                .build();
    }
}
