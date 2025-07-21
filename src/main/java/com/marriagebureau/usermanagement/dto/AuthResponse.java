// src/main/java/com/marriagebureau/usermanagement/dto/AuthResponse.java
package com.marriagebureau.usermanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor // Add this or a specific constructor
@NoArgsConstructor // Good to have a no-args constructor too
public class AuthResponse {
    private String token; // Or accessToken
    private String type = "Bearer";
    private Long id;
    private String email; // Or username
    private String role;
    private String message; // ⭐ NEW FIELD for error messages
}