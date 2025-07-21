package com.marriagebureau.dto;



import lombok.Data; // If not using Lombok, add getters, setters, and constructors.

@Data
public class MatchDTO {
    private Long id;
    private ClientSubDTO client1; // Use a nested DTO for client details
    private ClientSubDTO client2; // Use a nested DTO for client details
    private String date; // Consider using java.time.LocalDate
    private String status;
    private int compatibility;
    private double commission; // Use double or BigDecimal for currency

    // If not using Lombok, uncomment and generate these
    // public MatchDTO() {}
    // public MatchDTO(Long id, ClientSubDTO client1, ...) { ... }
    // public Long getId() { return id; }
    // ... etc.
}