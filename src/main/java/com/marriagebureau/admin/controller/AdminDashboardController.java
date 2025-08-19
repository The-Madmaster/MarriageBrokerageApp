package com.marriagebureau.admin.controller;

import com.marriagebureau.admin.dto.AdminDashboardStatsDTO;
import com.marriagebureau.admin.service.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/dashboard")
@PreAuthorize("hasRole('ADMIN')") // CRITICAL: Secures all endpoints for Admins only
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    /**
     * Retrieves system-wide statistics for the admin dashboard.
     * Only accessible by users with the ROLE_ADMIN.
     * @return A DTO containing various system metrics.
     */
    @GetMapping("/stats")
    public ResponseEntity<AdminDashboardStatsDTO> getDashboardStatistics() {
        AdminDashboardStatsDTO stats = adminDashboardService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }
}