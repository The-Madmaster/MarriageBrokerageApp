// src/main/java/com/marriagebureau/security/SecurityConfig.java
package com.marriagebureau.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

// ... other imports ...

@Configuration
@EnableWebSecurity
@EnableMethodSecurity


public class SecurityConfig {
    @Bean
public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
}

    // ... constructor and other fields ...

    // REMOVE THIS BEAN DEFINITION - PasswordEncoder will now be provided by ApplicationConfig
    /*
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    */ // <-- This block must remain commented out or be removed

    // ... other beans and methods ...
    
}