package com.marriagebureau.dto;


import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor; // If not using Lombok, add getters, setters, and constructors.

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuggestedMatchDTO {
    private Long id;
    private String name;
    private int age;
    private String occupation;
    private String avatar; // URL for the avatar
    private int matchScore;

    // If not using Lombok, uncomment and generate these
    // public SuggestedMatchDTO() {}
    // public SuggestedMatchDTO(Long id, String name, int age, String occupation, String avatar, int matchScore) { ... }
    // public Long getId() { return id; }
    // ... etc.
}