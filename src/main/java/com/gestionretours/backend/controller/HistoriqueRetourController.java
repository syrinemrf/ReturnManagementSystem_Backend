package com.gestionretours.backend.controller;

import com.gestionretours.backend.model.dto.response.HistoriqueRetourResponse;
import com.gestionretours.backend.service.HistoriqueRetourService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Consultation de l'historique des retours — lecture seule, pas de création ici
@RestController
@RequestMapping("/api/historique")
@Tag(name = "Historique", description = "Return history / Historique des retours")
@RequiredArgsConstructor
public class HistoriqueRetourController {

    private final HistoriqueRetourService historiqueRetourService;

    @GetMapping("/retour/{retourId}")
    @Operation(summary = "Get history by return ID / Obtenir l'historique par ID de retour",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success / Succès"),
                    @ApiResponse(responseCode = "404", description = "Return not found / Retour introuvable")
            })
    public ResponseEntity<List<HistoriqueRetourResponse>> findByRetourId(@PathVariable Long retourId) {
        return ResponseEntity.ok(historiqueRetourService.findByRetourId(retourId));
    }

    @GetMapping("/recent")
    @Operation(summary = "Get recent activity / Obtenir l'activité récente",
            responses = @ApiResponse(responseCode = "200", description = "Success / Succès"))
    public ResponseEntity<List<HistoriqueRetourResponse>> findRecent() {
        return ResponseEntity.ok(historiqueRetourService.findRecent());
    }
}
