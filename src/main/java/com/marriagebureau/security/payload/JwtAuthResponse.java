package com.marriagebureau.security.payload;

import lombok.Data;

@Data // Lombok annotation to generate getters, setters, toString, equals, and hashCode
public class JwtAuthResponse {
    private String accessToken;
    private String tokenType = "Bearer"; // Standard type for JWTs

    public JwtAuthResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}