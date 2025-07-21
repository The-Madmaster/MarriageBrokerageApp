package com.marriagebureau.dto;


import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor; // If not using Lombok, add getters, setters, and constructors.

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDTO {
    private Long id;
    private String name;
    private int age;
    private String image; // URL to client's avatar/image
    private String religion;
    private String caste;
    private String occupation;
    private String status;
    private String lastContact; // Consider using java.time.LocalDate if storing as Date
    private int compatibilityScore;

    // If not using Lombok, uncomment and generate these
    // public ClientDTO() {}
    // public ClientDTO(Long id, String name, int age, String image, ...) { ... }
    // public Long getId() { return id; }
    // ... etc.
}