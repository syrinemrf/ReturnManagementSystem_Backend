package com.gestionretours.backend.converter;

import com.gestionretours.backend.model.dto.response.UtilisateurResponse;
import com.gestionretours.backend.model.entity.Utilisateur;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Converter for Utilisateur entity and its DTOs.
 * Convertisseur pour l'entité Utilisateur et ses DTOs.
 */
@Component
public class UtilisateurConverter {

    private final ModelMapper modelMapper;

    public UtilisateurConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * Converts a Utilisateur entity to its response DTO.
     * Convertit une entité Utilisateur en DTO de réponse.
     */
    public UtilisateurResponse toDto(Utilisateur entity) {
        if (entity == null) return null;
        return modelMapper.map(entity, UtilisateurResponse.class);
    }
}
