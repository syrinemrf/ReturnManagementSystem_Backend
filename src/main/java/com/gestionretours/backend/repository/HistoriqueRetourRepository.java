package com.gestionretours.backend.repository;

import com.gestionretours.backend.model.entity.HistoriqueRetour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Accès BDD pour l'historique — utilisé pour l'audit et l'activité récente du dashboard
@Repository
public interface HistoriqueRetourRepository extends JpaRepository<HistoriqueRetour, Long> {

    List<HistoriqueRetour> findByRetour_IdOrderByDateDesc(Long retourId);

    List<HistoriqueRetour> findTop10ByOrderByDateDesc();
}
