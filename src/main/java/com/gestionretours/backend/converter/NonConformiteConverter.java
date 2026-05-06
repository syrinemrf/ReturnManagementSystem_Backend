package com.gestionretours.backend.converter;

import com.gestionretours.backend.model.dto.request.NonConformiteRequest;
import com.gestionretours.backend.model.dto.response.NonConformiteResponse;
import com.gestionretours.backend.model.entity.NonConformite;
import com.gestionretours.backend.model.enums.Gravite;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Converter for NonConformite entity and its DTOs.
 * Convertisseur pour l'entité NonConformite et ses DTOs.
 */
@Component
public class NonConformiteConverter {

    private final ModelMapper modelMapper;

    public NonConformiteConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        // Skip nested relation fields to avoid LazyInitializationException
        this.modelMapper.typeMap(NonConformite.class, NonConformiteResponse.class)
                .addMappings(mapper -> {
                    mapper.skip(NonConformiteResponse::setRetourId);
                    mapper.skip(NonConformiteResponse::setRetourProduit);
                });
    }

    /**
     * Converts a NonConformite entity to its response DTO.
     * Convertit une entité NonConformite en DTO de réponse.
     */
    public NonConformiteResponse toDto(NonConformite entity) {
        if (entity == null) return null;
        NonConformiteResponse dto = modelMapper.map(entity, NonConformiteResponse.class);
        if (entity.getRetour() != null) {
            dto.setRetourId(entity.getRetour().getId());
            dto.setRetourProduit(entity.getRetour().getProduit());
        }
        return dto;
    }

    /**
     * Populates a new NonConformite entity from the request DTO fields.
     * Remplit une nouvelle entité NonConformite à partir des champs du DTO de requête.
     */
    public NonConformite toEntity(NonConformiteRequest request) {
        if (request == null) return null;
        NonConformite entity = new NonConformite();
        entity.setDescription(request.getDescription());
        entity.setGravite(request.getGravite() != null ? request.getGravite() : Gravite.MOYENNE);
        entity.setProduit(request.getProduit());
        return entity;
    }

    /**
     * Updates mutable fields on an existing entity from the request DTO.
     * Met à jour les champs modifiables d'une entité existante à partir du DTO de requête.
     */
    public void updateEntityFromRequest(NonConformiteRequest request, NonConformite entity) {
        entity.setDescription(request.getDescription());
        entity.setProduit(request.getProduit());
        if (request.getGravite() != null) {
            entity.setGravite(request.getGravite());
        }
    }
}
