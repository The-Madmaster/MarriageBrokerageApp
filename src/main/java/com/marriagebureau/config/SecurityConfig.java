// src/main/java/com/marriagebureau/config/SecurityConfig.java
package com.marriagebureau.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Marks this class as a Spring configuration class
@EnableWebSecurity(debug=true) // Enables Spring Security's web security support
public class SecurityConfig {

    // This bean defines the security filter chain, customizing security rules
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/users/**").permitAll() // Allow all requests to /api/users/** (our registration/get users endpoints)
                .requestMatchers("/", "/about").permitAll() // Also allow our home and about pages
                .anyRequest().authenticated() // All other requests require authentication
            )
            .csrf(csrf -> csrf.disable()); // Disable CSRF for simplicity in API testing (re-enable for production web apps)
        return http.build();
    }

    // You already have the BCryptPasswordEncoder bean in your main application class, so no need to repeat it here.
    // If you prefer to have all security-related beans in one place, you could move the BCryptPasswordEncoder bean here.
}