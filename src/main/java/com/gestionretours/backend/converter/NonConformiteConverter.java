package com.gestionretours.backend.converter;

import com.gestionretours.backend.model.dto.request.NonConformiteRequest;
import com.gestionretours.backend.model.dto.response.NonConformiteResponse;
import com.gestionretours.backend.model.entity.NonConformite;
import com.gestionretours.backend.model.enums.Gravite;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

// Convertit entre NonConformite et ses DTOs
// Les champs liés au retour parent sont gérés manuellement
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

    // On récupère le retourId et le nom du produit concerné directement depuis la relation
    public NonConformiteResponse toDto(NonConformite entity) {
        if (entity == null) return null;
        NonConformiteResponse dto = modelMapper.map(entity, NonConformiteResponse.class);
        if (entity.getRetour() != null) {
            dto.setRetourId(entity.getRetour().getId());
            dto.setRetourProduit(entity.getRetour().getProduit());
        }
        return dto;
    }

    // Gravité par défaut à MOYENNE si non précisée dans la requête
    public NonConformite toEntity(NonConformiteRequest request) {
        if (request == null) return null;
        NonConformite entity = new NonConformite();
        entity.setDescription(request.getDescription());
        entity.setGravite(request.getGravite() != null ? request.getGravite() : Gravite.MOYENNE);
        entity.setProduit(request.getProduit());
        return entity;
    }

    // On ne met à jour que la description, le produit et la gravité — le retour parent ne change pas
    public void updateEntityFromRequest(NonConformiteRequest request, NonConformite entity) {
        entity.setDescription(request.getDescription());
        entity.setProduit(request.getProduit());
        if (request.getGravite() != null) {
            entity.setGravite(request.getGravite());
        }
    }
}
