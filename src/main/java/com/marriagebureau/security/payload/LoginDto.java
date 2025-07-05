package com.marriagebureau.security.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor // Crucial for providing a constructor that takes all arguments
public class LoginDto {
    private String username;
    private String password;
}