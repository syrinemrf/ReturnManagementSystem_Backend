package com.gestionretours.backend.service;

import com.gestionretours.backend.model.dto.request.ChangerEtatRequest;
import com.gestionretours.backend.model.dto.request.RetourRequest;
import com.gestionretours.backend.model.dto.response.RetourProduitResponse;
import com.gestionretours.backend.model.enums.EtatTraitement;

import java.util.List;

/**
 * Product return service interface / Interface du service de retour produit
 */
public interface RetourProduitService {

    List<RetourProduitResponse> findAll();

    RetourProduitResponse findById(Long id);

    List<RetourProduitResponse> findByEtat(EtatTraitement etat);

    RetourProduitResponse create(RetourRequest request);

    RetourProduitResponse update(Long id, RetourRequest request);

    void delete(Long id);

    RetourProduitResponse changerEtat(Long id, ChangerEtatRequest request);
}
