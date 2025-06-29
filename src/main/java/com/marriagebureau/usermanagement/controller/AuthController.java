package com.marriagebureau.usermanagement.controller;

import com.marriagebureau.security.JwtTokenProvider;
import com.marriagebureau.security.payload.JwtAuthResponse;
import com.marriagebureau.security.payload.LoginDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Handles user login and generates a JWT.
     * POST /api/auth/login
     * @param loginDto The DTO containing username and password.
     * @return ResponseEntity with JwtAuthResponse containing the JWT token.
     */
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> authenticateUser(@RequestBody LoginDto loginDto) {
        // Authenticate the user using Spring Security's AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
        );

        // Set the authenticated user in Spring Security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token for the authenticated user
        String token = jwtTokenProvider.generateToken(authentication);

        // Return the token in a response DTO
        return ResponseEntity.ok(new JwtAuthResponse(token));
    }
}