package com.gestionretours.backend.converter;

import com.gestionretours.backend.model.dto.response.HistoriqueRetourResponse;
import com.gestionretours.backend.model.entity.HistoriqueRetour;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Converter for HistoriqueRetour entity and its DTOs.
 * Convertisseur pour l'entité HistoriqueRetour et ses DTOs.
 */
@Component
public class HistoriqueRetourConverter {

    private final ModelMapper modelMapper;

    public HistoriqueRetourConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        // Skip nested relation fields to avoid LazyInitializationException
        this.modelMapper.typeMap(HistoriqueRetour.class, HistoriqueRetourResponse.class)
                .addMappings(mapper -> {
                    mapper.skip(HistoriqueRetourResponse::setRetourId);
                    mapper.skip(HistoriqueRetourResponse::setEmployeNom);
                });
    }

    /**
     * Converts a HistoriqueRetour entity to its response DTO.
     * Convertit une entité HistoriqueRetour en DTO de réponse.
     */
    public HistoriqueRetourResponse toDto(HistoriqueRetour entity) {
        if (entity == null) return null;
        HistoriqueRetourResponse dto = modelMapper.map(entity, HistoriqueRetourResponse.class);
        if (entity.getRetour() != null) {
            dto.setRetourId(entity.getRetour().getId());
        }
        if (entity.getEmploye() != null) {
            dto.setEmployeNom(entity.getEmploye().getNom());
        }
        return dto;
    }
}
