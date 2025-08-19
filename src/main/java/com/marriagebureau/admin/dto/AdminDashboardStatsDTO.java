package com.marriagebureau.admin.dto;

import lombok.Builder;
import lombok.Data;

/**
 * DTO to hold statistics for the admin dashboard.
 */
@Data
@Builder
public class AdminDashboardStatsDTO {
    private long totalBrokers;
    private long totalClientProfiles;
    private long totalInterestsSent;
    private long interestsAccepted;
    private long interestsRejected;
    private long interestsPending;
}