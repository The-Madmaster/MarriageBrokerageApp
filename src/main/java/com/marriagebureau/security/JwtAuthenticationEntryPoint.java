package com.marriagebureau.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper; // Import ObjectMapper
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper(); // Initialize ObjectMapper

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        // This is invoked when a user tries to access a secured REST resource without supplying any credentials
        // or with invalid credentials.
        // We will send a 401 Unauthorized response with a JSON body.

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json"); // Set content type to JSON
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", System.currentTimeMillis());
        errorDetails.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        errorDetails.put("error", "Unauthorized");
        errorDetails.put("message", authException.getMessage()); // Get the specific exception message
        errorDetails.put("path", request.getRequestURI());

        // Write the JSON response to the output stream
        objectMapper.writeValue(response.getWriter(), errorDetails);
    }
}
