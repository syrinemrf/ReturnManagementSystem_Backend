package com.gestionretours.backend.controller;

import com.gestionretours.backend.model.dto.request.RegisterRequest;
import com.gestionretours.backend.model.dto.response.UtilisateurResponse;
import com.gestionretours.backend.service.UtilisateurService;
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

// Gestion des utilisateurs — accessible uniquement aux admins
@RestController
@RequestMapping("/api/utilisateurs")
@Tag(name = "Utilisateurs", description = "User management / Gestion des utilisateurs")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:80"})
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    @GetMapping
    @Operation(summary = "Get all users / Obtenir tous les utilisateurs",
            responses = @ApiResponse(responseCode = "200", description = "Success / Succès"))
    public ResponseEntity<List<UtilisateurResponse>> findAll() {
        return ResponseEntity.ok(utilisateurService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID / Obtenir un utilisateur par ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Found / Trouvé"),
                    @ApiResponse(responseCode = "404", description = "Not found / Introuvable")
            })
    public ResponseEntity<UtilisateurResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(utilisateurService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Create user / Créer un utilisateur",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Created / Créé"),
                    @ApiResponse(responseCode = "400", description = "Validation error / Erreur de validation")
            })
    public ResponseEntity<UtilisateurResponse> create(@Valid @RequestBody RegisterRequest request) {
        return new ResponseEntity<>(utilisateurService.create(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user / Mettre à jour un utilisateur",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Updated / Mis à jour"),
                    @ApiResponse(responseCode = "404", description = "Not found / Introuvable")
            })
    public ResponseEntity<UtilisateurResponse> update(@PathVariable Long id,
                                                      @Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(utilisateurService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user / Supprimer un utilisateur",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Deleted / Supprimé"),
                    @ApiResponse(responseCode = "404", description = "Not found / Introuvable")
            })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        utilisateurService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
