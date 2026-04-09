package com.gestionretours.backend.controller;

import com.gestionretours.backend.model.dto.request.ChangerEtatRequest;
import com.gestionretours.backend.model.dto.request.RetourRequest;
import com.gestionretours.backend.model.dto.response.RetourProduitResponse;
import com.gestionretours.backend.model.enums.EtatTraitement;
import com.gestionretours.backend.service.RetourProduitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Product returns controller / Contrôleur des retours produits
 */
@RestController
@RequestMapping("/api/retours")
@Tag(name = "Retours", description = "Product return management / Gestion des retours produits")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:80"})
@RequiredArgsConstructor
public class RetourProduitController {

    private final RetourProduitService retourProduitService;

    @GetMapping
    @Operation(summary = "Get all returns / Obtenir tous les retours",
            responses = @ApiResponse(responseCode = "200", description = "Success / Succès"))
    public ResponseEntity<List<RetourProduitResponse>> findAll() {
        return ResponseEntity.ok(retourProduitService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get return by ID / Obtenir un retour par ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Found / Trouvé"),
                    @ApiResponse(responseCode = "404", description = "Not found / Introuvable")
            })
    public ResponseEntity<RetourProduitResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(retourProduitService.findById(id));
    }

    @GetMapping("/etat/{etat}")
    @Operation(summary = "Get returns by state / Obtenir les retours par état",
            responses = @ApiResponse(responseCode = "200", description = "Success / Succès"))
    public ResponseEntity<List<RetourProduitResponse>> findByEtat(@PathVariable EtatTraitement etat) {
        return ResponseEntity.ok(retourProduitService.findByEtat(etat));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('EMPLOYE','QUALITE','ADMIN')")
    @Operation(summary = "Create a new return / Créer un nouveau retour",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Created / Créé"),
                    @ApiResponse(responseCode = "400", description = "Validation error / Erreur de validation")
            })
    public ResponseEntity<RetourProduitResponse> create(@Valid @RequestBody RetourRequest request) {
        return new ResponseEntity<>(retourProduitService.create(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('QUALITE','ADMIN')")
    @Operation(summary = "Update a return / Mettre à jour un retour",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Updated / Mis à jour"),
                    @ApiResponse(responseCode = "404", description = "Not found / Introuvable")
            })
    public ResponseEntity<RetourProduitResponse> update(@PathVariable Long id,
                                                        @Valid @RequestBody RetourRequest request) {
        return ResponseEntity.ok(retourProduitService.update(id, request));
    }

    @PutMapping("/{id}/etat")
    @PreAuthorize("hasAnyRole('QUALITE','ADMIN')")
    @Operation(summary = "Change return state / Changer l'état d'un retour",
            responses = {
                    @ApiResponse(responseCode = "200", description = "State changed / État changé"),
                    @ApiResponse(responseCode = "404", description = "Not found / Introuvable")
            })
    public ResponseEntity<RetourProduitResponse> changerEtat(@PathVariable Long id,
                                                             @Valid @RequestBody ChangerEtatRequest request) {
        return ResponseEntity.ok(retourProduitService.changerEtat(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a return / Supprimer un retour",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Deleted / Supprimé"),
                    @ApiResponse(responseCode = "404", description = "Not found / Introuvable")
            })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        retourProduitService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
