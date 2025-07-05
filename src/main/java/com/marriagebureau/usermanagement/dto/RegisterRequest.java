// src/main/java/com/marriagebureau/usermanagement/dto/RegisterRequest.java
package com.marriagebureau.usermanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    
    
    private String username;        // <--- THIS FIELD MUST EXIST for getUsername() to work
    
    private String contactNumber;   // <--- THIS FIELD MUST EXIST for getContactNumber() to work

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;
}