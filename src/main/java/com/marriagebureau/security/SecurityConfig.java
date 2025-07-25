// src/main/java/com/marriagebureau/security/SecurityConfig.java
package com.marriagebureau.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider; // NEW: Import AuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity // Enables Spring Security's web security support
@EnableMethodSecurity // Enables @PreAuthorize, @PostAuthorize, @Secured, @RolesAllowed annotations
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider; // NEW: Declare AuthenticationProvider

    // Constructor to inject the JwtAuthenticationEntryPoint, JwtAuthenticationFilter,
    // and AuthenticationProvider.
    // Spring will automatically inject these as they are @Components/@Beans
    public SecurityConfig(
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            AuthenticationProvider authenticationProvider // NEW: Inject AuthenticationProvider
    ) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authenticationProvider = authenticationProvider; // NEW: Assign to field
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        // Exposes the AuthenticationManager as a Bean for direct use (e.g., in AuthController)
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF for stateless REST APIs, as JWTs are inherently protected against CSRF attacks
            .csrf(AbstractHttpConfigurer::disable)
            // Configure exception handling for authentication failures
            .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))
            // Set session management to stateless, meaning no HTTP session will be created or used by Spring Security
            // This is crucial for a JWT-based application
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // Define authorization rules for HTTP requests
            .authorizeHttpRequests(authorize -> authorize
                // Permit access to authentication endpoints (login, register, etc.)
                .requestMatchers("/api/auth/**").permitAll()
                // Permit access to H2 Console for development/testing
                // IMPORTANT: In production, either remove this or restrict access significantly
                .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                // Permit access to common static resources (if serving a frontend directly from Spring Boot)
                .requestMatchers(
                    AntPathRequestMatcher.antMatcher("/favicon.ico"),
                    AntPathRequestMatcher.antMatcher("/"),
                    AntPathRequestMatcher.antMatcher("/index.html"),
                    AntPathRequestMatcher.antMatcher("/static/**"), // Catch all under static
                    AntPathRequestMatcher.antMatcher("/css/**"),
                    AntPathRequestMatcher.antMatcher("/js/**"),
                    AntPathRequestMatcher.antMatcher("/images/**")
                ).permitAll()
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            // NEW: Add the AuthenticationProvider to the HttpSecurity configuration
            // This explicitly tells Spring Security to use your custom provider
            .authenticationProvider(authenticationProvider)
            // Add the JWT filter before the UsernamePasswordAuthenticationFilter in the Spring Security filter chain.
            // This ensures that the JWT is processed and the SecurityContext is populated before Spring Security's
            // default authentication mechanisms kick in, which would otherwise try to authenticate based on sessions/cookies.
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // Enable H2 Console frames for proper display within a browser
        // This is necessary because H2 Console uses iframes, and Spring Security's defaults might block them
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));

        return http.build();
    }
}