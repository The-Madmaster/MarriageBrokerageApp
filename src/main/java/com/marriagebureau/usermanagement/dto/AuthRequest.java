// src/main/java/com/marriagebureau/usermanagement/dto/AuthRequest.java
package com.marriagebureau.usermanagement.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {
    private String email;
    private String password;
}