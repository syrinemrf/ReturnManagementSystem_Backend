package com.gestionretours.backend.repository;

import com.gestionretours.backend.model.entity.NonConformite;
import com.gestionretours.backend.model.enums.Gravite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Accès BDD pour les non-conformités
@Repository
public interface NonConformiteRepository extends JpaRepository<NonConformite, Long> {

    List<NonConformite> findByGravite(Gravite gravite);

    Long countByGravite(Gravite gravite);

    List<NonConformite> findByRetour_Id(Long retourId);
}
