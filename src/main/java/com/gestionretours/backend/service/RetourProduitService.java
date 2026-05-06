package com.gestionretours.backend.service;

import com.gestionretours.backend.converter.HistoriqueRetourConverter;
import com.gestionretours.backend.converter.RetourProduitConverter;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RetourProduitService {

    private final RetourProduitRepository retourProduitRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final HistoriqueRetourRepository historiqueRetourRepository;
    private final RetourProduitConverter retourProduitConverter;
    private final HistoriqueRetourConverter historiqueRetourConverter;

    @Transactional(readOnly = true)
    public List<RetourProduitResponse> findAll() {
        return retourProduitRepository.findAll()
                .stream()
                .map(retourProduitConverter::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RetourProduitResponse findById(Long id) {
        return retourProduitRepository.findById(id)
                .map(retourProduitConverter::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Retour introuvable avec l'id: " + id));
    }

    @Transactional(readOnly = true)
    public List<RetourProduitResponse> findByEtat(EtatTraitement etat) {
        return retourProduitRepository.findByEtatTraitement(etat)
                .stream()
                .map(retourProduitConverter::toDto)
                .collect(Collectors.toList());
    }

    @CacheEvict(value = "dashboardStats", allEntries = true)
    @Transactional
    public RetourProduitResponse create(RetourRequest request) {
        Utilisateur utilisateur = null;
        if (request.getUtilisateurId() != null) {
            utilisateur = utilisateurRepository.findById(request.getUtilisateurId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Utilisateur introuvable avec l'id: " + request.getUtilisateurId()));
        }

        RetourProduit retour = retourProduitConverter.toEntity(request);
        retour.setUtilisateur(utilisateur);
        retour = retourProduitRepository.save(retour);

        HistoriqueRetour historique = new HistoriqueRetour();
        historique.setRetour(retour);
        historique.setAction("Retour créé");
        historique.setEmploye(utilisateur);
        historique.setNouvelEtat(EtatTraitement.EN_ATTENTE);
        historiqueRetourRepository.save(historique);

        log.debug("Retour créé avec l'id: {}", retour.getId());
        return retourProduitConverter.toDto(retour);
    }

    @CacheEvict(value = "dashboardStats", allEntries = true)
    @Transactional
    public RetourProduitResponse update(Long id, RetourRequest request) {
        RetourProduit retour = retourProduitRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Retour introuvable avec l'id: " + id));

        retourProduitConverter.updateEntityFromRequest(request, retour);

        if (request.getUtilisateurId() != null) {
            Utilisateur utilisateur = utilisateurRepository.findById(request.getUtilisateurId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Utilisateur introuvable avec l'id: " + request.getUtilisateurId()));
            retour.setUtilisateur(utilisateur);
        }

        retour = retourProduitRepository.save(retour);
        return retourProduitConverter.toDto(retour);
    }

    @CacheEvict(value = "dashboardStats", allEntries = true)
    @Transactional
    public void delete(Long id) {
        retourProduitRepository.findById(id).ifPresentOrElse(
                retour -> retourProduitRepository.deleteById(id),
                () -> { throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Retour introuvable avec l'id: " + id); }
        );
        log.debug("Retour supprimé avec l'id: {}", id);
    }

    @CacheEvict(value = "dashboardStats", allEntries = true)
    @Transactional
    public RetourProduitResponse changerEtat(Long id, ChangerEtatRequest request) {
        RetourProduit retour = retourProduitRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Retour introuvable avec l'id: " + id));

        EtatTraitement ancienEtat = retour.getEtatTraitement();
        retour.setEtatTraitement(request.getNouvelEtat());
        retour = retourProduitRepository.save(retour);

        Utilisateur employe = null;
        if (request.getEmployeId() != null) {
            employe = utilisateurRepository.findById(request.getEmployeId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Utilisateur introuvable avec l'id: " + request.getEmployeId()));
        }

        String action = (request.getCommentaire() != null && !request.getCommentaire().isBlank())
                ? request.getCommentaire()
                : String.format("État changé de %s à %s", ancienEtat, request.getNouvelEtat());

        HistoriqueRetour historique = new HistoriqueRetour();
        historique.setRetour(retour);
        historique.setAction(action);
        historique.setEmploye(employe);
        historique.setAncienEtat(ancienEtat);
        historique.setNouvelEtat(request.getNouvelEtat());
        historiqueRetourRepository.save(historique);

        log.debug("État du retour {} changé de {} à {}", id, ancienEtat, request.getNouvelEtat());
        return retourProduitConverter.toDto(retour);
    }
}
