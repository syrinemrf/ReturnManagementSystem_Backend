package com.gestionretours.backend.controller;

import com.gestionretours.backend.model.dto.request.NonConformiteRequest;
import com.gestionretours.backend.model.dto.response.NonConformiteResponse;
import com.gestionretours.backend.service.NonConformiteService;
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

// Gestion des non-conformités — création réservée aux rôles qualité/admin
@RestController
@RequestMapping("/api/non-conformites")
@Tag(name = "Non-Conformites", description = "Non-conformity management / Gestion des non-conformités")
@RequiredArgsConstructor
public class NonConformiteController {

    private final NonConformiteService nonConformiteService;

    @GetMapping
    @Operation(summary = "Get all non-conformities / Obtenir toutes les non-conformités",
            responses = @ApiResponse(responseCode = "200", description = "Success / Succès"))
    public ResponseEntity<List<NonConformiteResponse>> findAll() {
        return ResponseEntity.ok(nonConformiteService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get non-conformity by ID / Obtenir une non-conformité par ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Found / Trouvé"),
                    @ApiResponse(responseCode = "404", description = "Not found / Introuvable")
            })
    public ResponseEntity<NonConformiteResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(nonConformiteService.findById(id));
    }

    @GetMapping("/retour/{retourId}")
    @Operation(summary = "Get non-conformities by return ID / Obtenir les non-conformités par ID de retour",
            responses = @ApiResponse(responseCode = "200", description = "Success / Succès"))
    public ResponseEntity<List<NonConformiteResponse>> findByRetourId(@PathVariable Long retourId) {
        return ResponseEntity.ok(nonConformiteService.findByRetourId(retourId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('QUALITE','ADMIN')")
    @Operation(summary = "Create non-conformity / Créer une non-conformité",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Created / Créé"),
                    @ApiResponse(responseCode = "400", description = "Validation error / Erreur de validation")
            })
    public ResponseEntity<NonConformiteResponse> create(@Valid @RequestBody NonConformiteRequest request) {
        return new ResponseEntity<>(nonConformiteService.create(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('QUALITE','ADMIN')")
    @Operation(summary = "Update non-conformity / Mettre à jour une non-conformité",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Updated / Mis à jour"),
                    @ApiResponse(responseCode = "404", description = "Not found / Introuvable")
            })
    public ResponseEntity<NonConformiteResponse> update(@PathVariable Long id,
                                                        @Valid @RequestBody NonConformiteRequest request) {
        return ResponseEntity.ok(nonConformiteService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete non-conformity / Supprimer une non-conformité",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Deleted / Supprimé"),
                    @ApiResponse(responseCode = "404", description = "Not found / Introuvable")
            })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        nonConformiteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
