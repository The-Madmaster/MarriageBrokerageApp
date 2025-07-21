package com.marriagebureau.dto;


// Use Lombok for less boilerplate code if you have it, otherwise add getters/setters/constructors manually.
// If not using Lombok, remove @Data
import lombok.Data;

@Data // Requires Lombok dependency. If not using Lombok, add getters, setters, and constructors.
public class DashboardStatsDTO {
    private int activeClients;
    private int successfulMatches;
    private double monthlyRevenue; // Use double or BigDecimal for currency
    private int avgMatchScore;

    // If not using Lombok, uncomment and generate these:
    // public DashboardStatsDTO() {}
    // public DashboardStatsDTO(int activeClients, int successfulMatches, double monthlyRevenue, int avgMatchScore) {
    //     this.activeClients = activeClients;
    //     this.successfulMatches = successfulMatches;
    //     this.monthlyRevenue = monthlyRevenue;
    //     this.avgMatchScore = avgMatchScore;
    // }
    // public int getActiveClients() { return activeClients; }
    // public void setActiveClients(int activeClients) { this.activeClients = activeClients; }
    // ... and so on for other fields
}