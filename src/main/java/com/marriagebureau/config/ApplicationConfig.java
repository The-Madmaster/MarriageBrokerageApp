// src/main/java/com/marriagebureau/config/ApplicationConfig.java
package com.marriagebureau.config;

import com.marriagebureau.usermanagement.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder; // Make sure this import is present

@Configuration
public class ApplicationConfig {

    @Autowired
    private AppUserRepository appUserRepository;

    // 1. Inject PasswordEncoder
    private final PasswordEncoder passwordEncoder; // Declare field

    @Autowired // Add constructor to inject PasswordEncoder and AppUserRepository
    public ApplicationConfig(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder; // Initialize the injected passwordEncoder
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> appUserRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }

    // You correctly removed the @Bean public PasswordEncoder method from here.

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        // 2. Use the injected passwordEncoder field
        authProvider.setPasswordEncoder(passwordEncoder); // Changed from passwordEncoder() to passwordEncoder
        return authProvider;
    }

    // You correctly removed the @Bean public AuthenticationManager method from here.
    // The AuthenticationManager is now solely defined in SecurityConfig.
}