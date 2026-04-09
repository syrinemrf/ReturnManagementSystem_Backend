package com.gestionretours.backend.service;

import com.gestionretours.backend.model.dto.response.DashboardStatsResponse;

/**
 * Dashboard service interface / Interface du service tableau de bord
 */
public interface DashboardService {

    DashboardStatsResponse getStats();
}
