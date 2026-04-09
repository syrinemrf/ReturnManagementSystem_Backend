package com.gestionretours.backend.service;

import com.gestionretours.backend.model.dto.request.NonConformiteRequest;
import com.gestionretours.backend.model.dto.response.NonConformiteResponse;

import java.util.List;

/**
 * Non-conformity service interface / Interface du service de non-conformité
 */
public interface NonConformiteService {

    List<NonConformiteResponse> findAll();

    NonConformiteResponse findById(Long id);

    List<NonConformiteResponse> findByRetourId(Long retourId);

    NonConformiteResponse create(NonConformiteRequest request);

    NonConformiteResponse update(Long id, NonConformiteRequest request);

    void delete(Long id);
}
