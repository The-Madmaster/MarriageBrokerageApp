// src/main/java/com/marriagebureau/usermanagement/dto/AuthResponse.java
package com.marriagebureau.usermanagement.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private Long userId; // Or profileId, depending on what you want to return
    private String message; // For success/error messages
}