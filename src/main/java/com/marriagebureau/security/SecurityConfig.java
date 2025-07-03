    // src/main/java/com/marriagebureau/config/SecurityConfig.java
    package com.marriagebureau.config;

    import com.marriagebureau.security.JwtAuthenticationFilter; // Import your JWT filter
    import lombok.RequiredArgsConstructor;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.authentication.AuthenticationProvider;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.http.SessionCreationPolicy;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

    @Configuration // Marks this class as a source of bean definitions
    @EnableWebSecurity // Enables Spring Security's web security features
    @RequiredArgsConstructor // Lombok: Generates a constructor with required arguments (final fields)
    public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthFilter; // Injects the JWT authentication filter
        private final AuthenticationProvider authenticationProvider; // Injects the custom authentication provider

        // Defines the security filter chain, which configures HTTP security for different requests.
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                .csrf(csrf -> csrf.disable()) // Disable CSRF protection as we are using JWT (stateless)
                .authorizeHttpRequests(auth -> auth
                    // Allow unauthenticated access to authentication endpoints
                    .requestMatchers("/api/v1/auth/**").permitAll()
                    // Allow unauthenticated access to the H2 console (for development/debugging)
                    // Note: In production, you would secure or disable the H2 console.
                    .requestMatchers("/h2-console/**").permitAll()
                    // Allow access to static resources (e.g., index.html, CSS, JS)
                    .requestMatchers("/", "/index.html", "/static/**", "/css/**", "/js/**", "/images/**").permitAll()
                    // All other requests require authentication
                    .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                    // Set session creation policy to stateless, as JWTs are self-contained
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider) // Register the custom authentication provider
                // Add the JWT authentication filter before the UsernamePasswordAuthenticationFilter
                // This ensures JWTs are processed before Spring's default authentication
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

            // Enable H2 console frame options for proper display in a browser (if using H2)
            http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));

            return http.build(); // Build and return the SecurityFilterChain
        }
    }
    