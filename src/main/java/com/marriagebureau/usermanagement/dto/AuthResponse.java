// src/main/java/com/marriagebureau/usermanagement/dto/AuthResponse.java
package com.marriagebureau.usermanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Generates getters, setters, toString, equals, and hashCode
@NoArgsConstructor // Generates a no-argument constructor
@AllArgsConstructor // Generates a constructor with all fields
public class AuthResponse {
    private String accessToken; // Changed name from 'token' to 'accessToken'
    private String type = "Bearer"; // Default value for the token type
    private Long id;
    private String email;
    private String role; // Role of the authenticated user
}