package com.gestionretours.backend.repository;

import com.gestionretours.backend.model.entity.RetourProduit;
import com.gestionretours.backend.model.enums.EtatTraitement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

// Accès BDD pour les retours produits — inclut des requêtes personnalisées pour les stats
@Repository
public interface RetourProduitRepository extends JpaRepository<RetourProduit, Long> {

    List<RetourProduit> findByEtatTraitement(EtatTraitement etat);

    List<RetourProduit> findByClientContainingIgnoreCase(String client);

    Long countByEtatTraitement(EtatTraitement etat);

    List<RetourProduit> findTop5ByOrderByDateDesc();

    @Query("SELECT COUNT(r) FROM RetourProduit r WHERE YEAR(r.date) = :year AND MONTH(r.date) = :month")
    Long countByMonthAndYear(@Param("month") int month, @Param("year") int year);
}
