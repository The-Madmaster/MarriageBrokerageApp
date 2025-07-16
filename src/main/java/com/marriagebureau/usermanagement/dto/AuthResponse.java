// src/main/java/com/marriagebureau/usermanagement/dto/AuthResponse.java
package com.marriagebureau.usermanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor // Keep this annotation.
public class AuthResponse {
    private String accessToken;
    private String tokenType = "Bearer"; // This field will be included by @AllArgsConstructor
    private Long id;
    private String email;
    private String role;

    // REMOVE THE FOLLOWING MANUAL CONSTRUCTOR BLOCK COMPLETELY:
    /*
    public AuthResponse(String accessToken, Long id, String email, String role) {
        this.accessToken = accessToken;
        this.id = id;
        this.email = email;
        this.role = role;
    }
    */
}