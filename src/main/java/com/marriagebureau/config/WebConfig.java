package com.marriagebureau.config; // Adjust package as necessary

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Apply CORS to all endpoints
                .allowedOrigins("http://127.0.0.1:8080/", "http://127.0.0.1:3000") // <-- REPLACE WITH YOUR FRONTEND'S ACTUAL ORIGIN(S)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow common HTTP methods
                .allowedHeaders("*") // Allow all headers
                .allowCredentials(true); // Allow sending of cookies/authentication headers
    }
}