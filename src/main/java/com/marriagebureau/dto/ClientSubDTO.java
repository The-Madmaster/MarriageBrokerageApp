package com.marriagebureau.dto;



import lombok.Data; 
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor; // If not using Lombok, add getters, setters, and constructors.

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientSubDTO {
    private Long id;
    private String name;
    private String image; // URL to client's avatar/image

    // If not using Lombok, uncomment and generate these
    // public ClientSubDTO() {}
    // public ClientSubDTO(Long id, String name, String image) { ... }
    // public Long getId() { return id; }
    // ... etc.
}