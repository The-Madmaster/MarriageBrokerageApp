// src/main/java/com/marriagebureau/usermanagement/controller/AuthController.java
package com.marriagebureau.usermanagement.controller;

import com.marriagebureau.usermanagement.dto.LoginRequest;
import com.marriagebureau.usermanagement.dto.RegisterRequest;
import com.marriagebureau.usermanagement.dto.AuthResponse;
import com.marriagebureau.usermanagement.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        AuthResponse response = authService.registerUser(registerRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse response = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(response);
    }
}