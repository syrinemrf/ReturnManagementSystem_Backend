package com.gestionretours.backend.controller;

import com.gestionretours.backend.model.dto.response.DashboardStatsResponse;
import com.gestionretours.backend.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

// Retourne les statistiques agrégées pour le tableau de bord
@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard", description = "Dashboard statistics / Statistiques du tableau de bord")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    @PreAuthorize("hasAnyRole('QUALITE','ADMIN')")
    @Operation(summary = "Get dashboard statistics / Obtenir les statistiques du tableau de bord",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success / Succès"),
                    @ApiResponse(responseCode = "403", description = "Forbidden / Accès interdit")
            })
    public ResponseEntity<DashboardStatsResponse> getStats() {
        return ResponseEntity.ok(dashboardService.getStats());
    }
}
