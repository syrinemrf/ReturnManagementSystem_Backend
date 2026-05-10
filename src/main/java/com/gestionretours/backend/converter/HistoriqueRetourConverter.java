package com.gestionretours.backend.converter;

import com.gestionretours.backend.model.dto.response.HistoriqueRetourResponse;
import com.gestionretours.backend.model.entity.HistoriqueRetour;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

// Convertit entre HistoriqueRetour et son DTO
// On extrait manuellement le nom de l'employé et l'id du retour pour éviter les erreurs lazy
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

    // Conversion de l'entité vers le DTO — on remplit retourId et employeNom manuellement
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
