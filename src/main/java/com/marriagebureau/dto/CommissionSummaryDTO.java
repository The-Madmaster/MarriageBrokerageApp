package com.marriagebureau.dto;


import lombok.Data; // If not using Lombok, add getters, setters, and constructors.

@Data
public class CommissionSummaryDTO {
    private double thisMonth;
    private double target;
    private double pendingPayments;
    private int pendingMatches;

    // If not using Lombok, uncomment and generate these
    // public CommissionSummaryDTO() {}
    // public CommissionSummaryDTO(double thisMonth, double target, double pendingPayments, int pendingMatches) { ... }
    // public double getThisMonth() { return thisMonth; }
    // ... etc.
}