package com.gestionretours.backend.converter;

import com.gestionretours.backend.model.dto.request.RetourRequest;
import com.gestionretours.backend.model.dto.response.RetourProduitResponse;
import com.gestionretours.backend.model.entity.RetourProduit;
import com.gestionretours.backend.model.enums.EtatTraitement;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Converter for RetourProduit entity and its DTOs.
 * Convertisseur pour l'entité RetourProduit et ses DTOs.
 */
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

    /**
     * Converts a RetourProduit entity to its response DTO.
     * Convertit une entité RetourProduit en DTO de réponse.
     */
    public RetourProduitResponse toDto(RetourProduit entity) {
        if (entity == null) return null;
        RetourProduitResponse dto = modelMapper.map(entity, RetourProduitResponse.class);
        if (entity.getUtilisateur() != null) {
            dto.setUtilisateurNom(entity.getUtilisateur().getNom());
            dto.setUtilisateurId(entity.getUtilisateur().getId());
        }
        return dto;
    }

    /**
     * Populates a new RetourProduit entity from the request DTO fields.
     * Remplit une nouvelle entité RetourProduit à partir des champs du DTO de requête.
     */
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

    /**
     * Updates mutable fields on an existing entity from the request DTO.
     * Met à jour les champs modifiables d'une entité existante à partir du DTO de requête.
     */
    public void updateEntityFromRequest(RetourRequest request, RetourProduit entity) {
        entity.setProduit(request.getProduit());
        entity.setClient(request.getClient());
        entity.setRaison(request.getRaison());
        entity.setDescription(request.getDescription());
    }
}
