package com.gestionretours.backend.converter;

import com.gestionretours.backend.model.dto.response.UtilisateurResponse;
import com.gestionretours.backend.model.entity.Utilisateur;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

// Convertit entre l'entité Utilisateur et son DTO de réponse
@Component
public class UtilisateurConverter {

    private final ModelMapper modelMapper;

    public UtilisateurConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    // Conversion simple — ModelMapper s'occupe du mapping des champs
    public UtilisateurResponse toDto(Utilisateur entity) {
        if (entity == null) return null;
        return modelMapper.map(entity, UtilisateurResponse.class);
    }
}
