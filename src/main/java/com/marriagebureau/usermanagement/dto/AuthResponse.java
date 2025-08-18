package com.marriagebureau.usermanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder // <-- This is the missing annotation
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String accessToken;

    public String getTokenType() {
        return "Bearer";
    }
}