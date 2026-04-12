package com.gestionretours.backend.service.impl;

import com.gestionretours.backend.config.EntityMapper;
import com.gestionretours.backend.exception.ResourceNotFoundException;
import com.gestionretours.backend.model.dto.request.ChangerEtatRequest;
import com.gestionretours.backend.model.dto.request.RetourRequest;
import com.gestionretours.backend.model.dto.response.RetourProduitResponse;
import com.gestionretours.backend.model.entity.HistoriqueRetour;
import com.gestionretours.backend.model.entity.RetourProduit;
import com.gestionretours.backend.model.entity.Utilisateur;
import com.gestionretours.backend.model.enums.EtatTraitement;
import com.gestionretours.backend.repository.HistoriqueRetourRepository;
import com.gestionretours.backend.repository.RetourProduitRepository;
import com.gestionretours.backend.repository.UtilisateurRepository;
import com.gestionretours.backend.service.RetourProduitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Product return service implementation / Implémentation du service retour produit
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RetourProduitServiceImpl implements RetourProduitService {

    private final RetourProduitRepository retourProduitRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final HistoriqueRetourRepository historiqueRetourRepository;
    private final EntityMapper entityMapper;

    @Override
    @Transactional(readOnly = true)
    public List<RetourProduitResponse> findAll() {
        return retourProduitRepository.findAll()
                .stream()
                .map(entityMapper::toRetourResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public RetourProduitResponse findById(Long id) {
        RetourProduit retour = retourProduitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RetourProduit", "id", id));
        return entityMapper.toRetourResponse(retour);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RetourProduitResponse> findByEtat(EtatTraitement etat) {
        return retourProduitRepository.findByEtatTraitement(etat)
                .stream()
                .map(entityMapper::toRetourResponse)
                .collect(Collectors.toList());
    }

    @Override
    @CacheEvict(value = "dashboardStats", allEntries = true)
    @Transactional
    public RetourProduitResponse create(RetourRequest request) {
        Utilisateur utilisateur = null;
        if (request.getUtilisateurId() != null) {
            utilisateur = utilisateurRepository.findById(request.getUtilisateurId())
                    .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", request.getUtilisateurId()));
        }

        RetourProduit retour = RetourProduit.builder()
                .produit(request.getProduit())
                .client(request.getClient())
                .raison(request.getRaison())
                .description(request.getDescription())
                .etatTraitement(EtatTraitement.EN_ATTENTE)
                .utilisateur(utilisateur)
                .build();

        retour = retourProduitRepository.save(retour);

        // Auto-create history entry / Créer automatiquement une entrée d'historique
        HistoriqueRetour historique = HistoriqueRetour.builder()
                .retour(retour)
                .action("Retour créé / Return created")
                .employe(utilisateur)
                .nouvelEtat(EtatTraitement.EN_ATTENTE)
                .build();
        historiqueRetourRepository.save(historique);

        log.debug("Created RetourProduit with id: {}", retour.getId());
        return entityMapper.toRetourResponse(retour);
    }

    @Override
    @CacheEvict(value = "dashboardStats", allEntries = true)
    @Transactional
    public RetourProduitResponse update(Long id, RetourRequest request) {
        RetourProduit retour = retourProduitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RetourProduit", "id", id));

        retour.setProduit(request.getProduit());
        retour.setClient(request.getClient());
        retour.setRaison(request.getRaison());
        retour.setDescription(request.getDescription());

        if (request.getUtilisateurId() != null) {
            Utilisateur utilisateur = utilisateurRepository.findById(request.getUtilisateurId())
                    .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", request.getUtilisateurId()));
            retour.setUtilisateur(utilisateur);
        }

        retour = retourProduitRepository.save(retour);
        return entityMapper.toRetourResponse(retour);
    }

    @Override
    @CacheEvict(value = "dashboardStats", allEntries = true)
    @Transactional
    public void delete(Long id) {
        if (!retourProduitRepository.existsById(id)) {
            throw new ResourceNotFoundException("RetourProduit", "id", id);
        }
        retourProduitRepository.deleteById(id);
        log.debug("Deleted RetourProduit with id: {}", id);
    }

    @Override
    @CacheEvict(value = "dashboardStats", allEntries = true)
    @Transactional
    public RetourProduitResponse changerEtat(Long id, ChangerEtatRequest request) {
        RetourProduit retour = retourProduitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RetourProduit", "id", id));

        EtatTraitement ancienEtat = retour.getEtatTraitement();
        retour.setEtatTraitement(request.getNouvelEtat());
        retour = retourProduitRepository.save(retour);

        Utilisateur employe = null;
        if (request.getEmployeId() != null) {
            employe = utilisateurRepository.findById(request.getEmployeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", request.getEmployeId()));
        }

        String action = request.getCommentaire() != null && !request.getCommentaire().isBlank()
                ? request.getCommentaire()
                : String.format("État changé de %s à %s / State changed from %s to %s",
                ancienEtat, request.getNouvelEtat(), ancienEtat, request.getNouvelEtat());

        HistoriqueRetour historique = HistoriqueRetour.builder()
                .retour(retour)
                .action(action)
                .employe(employe)
                .ancienEtat(ancienEtat)
                .nouvelEtat(request.getNouvelEtat())
                .build();
        historiqueRetourRepository.save(historique);

        log.debug("Changed state of RetourProduit {} from {} to {}", id, ancienEtat, request.getNouvelEtat());
        return entityMapper.toRetourResponse(retour);
    }
}
