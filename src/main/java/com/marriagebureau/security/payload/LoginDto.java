package com.marriagebureau.security.payload;

import lombok.Data;

@Data // Lombok annotation to generate getters, setters, toString, equals, and hashCode
public class LoginDto {
    private String username;
    private String password;
}