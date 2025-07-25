package com.marriagebureau.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Apply CORS to all endpoints
                .allowedOrigins(
                    "https://reimagined-space-invention-r6xvpxq6qj42p75j-8080.app.github.dev", // Your backend's own URL (if accessed directly)
                    "https://reimagined-space-invention-r6xvpxq6qj42p75j-5173.app.github.dev"  // ⭐ Add your frontend's Vite development URL here
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Specify allowed HTTP methods
                .allowedHeaders("*") // Allow all headers
                .allowCredentials(true) // Allow sending of cookies and authentication headers
                .maxAge(3600); // Cache the preflight request for 1 hour
    }
}