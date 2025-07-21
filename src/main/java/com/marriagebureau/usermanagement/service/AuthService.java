// src/main/java/com/marriagebureau/usermanagement/service/AuthService.java
package com.marriagebureau.usermanagement.service;

import com.marriagebureau.security.JwtTokenProvider;
import com.marriagebureau.usermanagement.dto.AuthResponse;
import com.marriagebureau.usermanagement.dto.LoginRequest;
import com.marriagebureau.usermanagement.dto.RegisterRequest;
import com.marriagebureau.usermanagement.entity.AppUser;
import com.marriagebureau.usermanagement.repository.AppUserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthResponse register(RegisterRequest registerRequest) {
        if (appUserRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            // Updated to use the 6-argument constructor for consistency,
            // even if a dedicated error DTO is preferred for real errors.
            // Pass a message like "Email is already taken!"
            throw new RuntimeException("Email is already taken!"); // Still throwing exception here, handled in AuthController
        }

        AppUser user = AppUser.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .contactNumber(registerRequest.getContactNumber())
                .build();

        appUserRepository.save(user);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(registerRequest.getEmail(), registerRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtTokenProvider.generateToken(authentication);

        // ⭐ CORRECTED: Added the 6th argument (null for success messages)
        return new AuthResponse(accessToken, "Bearer", user.getId(), user.getUsername(), user.getRole().name(), null);
    }

    public AuthResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Ensure the principal is an AppUser object (or cast it correctly if it's Spring Security's UserDetails)
        // Note: Your UserDetailsService should return your AppUser class if you want direct access to its fields.
        AppUser authenticatedUser = (AppUser) authentication.getPrincipal();


        String accessToken = jwtTokenProvider.generateToken(authentication);

        // ⭐ CORRECTED: Added the 6th argument (null for success messages)
        return new AuthResponse(accessToken, "Bearer", authenticatedUser.getId(), authenticatedUser.getUsername(), authenticatedUser.getRole().name(), null);
    }

    public AuthResponse registerAdmin(RegisterRequest registerRequest) {
        if (appUserRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already taken!"); // Still throwing exception here
        }

        AppUser admin = AppUser.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .contactNumber(registerRequest.getContactNumber())
                .role(AppUser.Role.ROLE_ADMIN) // Explicitly set admin role
                .build();

        appUserRepository.save(admin);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(registerRequest.getEmail(), registerRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtTokenProvider.generateToken(authentication);

        // ⭐ CORRECTED: Added the 6th argument (null for success messages)
        return new AuthResponse(accessToken, "Bearer", admin.getId(), admin.getUsername(), admin.getRole().name(), null);
    }
}