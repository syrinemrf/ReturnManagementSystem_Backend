package com.gestionretours.backend.converter;

import com.gestionretours.backend.model.dto.request.RetourRequest;
import com.gestionretours.backend.model.dto.response.RetourProduitResponse;
import com.gestionretours.backend.model.entity.RetourProduit;
import com.gestionretours.backend.model.enums.EtatTraitement;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

// Convertit entre RetourProduit et ses DTOs
// Les champs liés aux relations (utilisateur) sont gérés manuellement pour éviter le LazyInitializationException
@Component
public class RetourProduitConverter {

    private final ModelMapper modelMapper;

    public RetourProduitConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        // Skip nested relation fields to avoid LazyInitializationException
        this.modelMapper.typeMap(RetourProduit.class, RetourProduitResponse.class)
                .addMappings(mapper -> {
                    mapper.skip(RetourProduitResponse::setUtilisateurNom);
                    mapper.skip(RetourProduitResponse::setUtilisateurId);
                });
    }

    // On mappe manuellement le nom et l'id de l'utilisateur après le mapping automatique
    public RetourProduitResponse toDto(RetourProduit entity) {
        if (entity == null) return null;
        RetourProduitResponse dto = modelMapper.map(entity, RetourProduitResponse.class);
        if (entity.getUtilisateur() != null) {
            dto.setUtilisateurNom(entity.getUtilisateur().getNom());
            dto.setUtilisateurId(entity.getUtilisateur().getId());
        }
        return dto;
    }

    // Crée une nouvelle entité à partir des données de la requête
    // L'état est forcé à EN_ATTENTE à la création, on ne fait pas confiance au client pour ça
    public RetourProduit toEntity(RetourRequest request) {
        if (request == null) return null;
        RetourProduit entity = new RetourProduit();
        entity.setProduit(request.getProduit());
        entity.setClient(request.getClient());
        entity.setRaison(request.getRaison());
        entity.setDescription(request.getDescription());
        entity.setEtatTraitement(EtatTraitement.EN_ATTENTE);
        return entity;
    }

    // Mise à jour des champs modifiables uniquement (l'état se change via changerEtat)
    public void updateEntityFromRequest(RetourRequest request, RetourProduit entity) {
        entity.setProduit(request.getProduit());
        entity.setClient(request.getClient());
        entity.setRaison(request.getRaison());
        entity.setDescription(request.getDescription());
    }
}
