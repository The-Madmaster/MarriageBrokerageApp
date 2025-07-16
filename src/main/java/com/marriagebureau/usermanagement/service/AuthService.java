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
            throw new RuntimeException("Email is already taken!");
        }

        AppUser user = AppUser.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .contactNumber(registerRequest.getContactNumber())
                .role(AppUser.Role.ROLE_USER) // Assign default role for registration
                .createdDate(LocalDateTime.now())
                .lastUpdatedDate(LocalDateTime.now())
                .enabled(true)
                .build();

        appUserRepository.save(user);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(registerRequest.getEmail(), registerRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);

        // Corrected AuthResponse constructor call to include "Bearer" tokenType
        return new AuthResponse(
            token,
            "Bearer", // tokenType
            user.getId(),
            user.getEmail(),
            user.getRole().name()
        );
    }

    public AuthResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (org.springframework.security.core.AuthenticationException e) {
            throw new RuntimeException("Invalid email or password", e);
        }


        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Cast to AppUser as it is your custom UserDetails implementation
        AppUser authenticatedUser = (AppUser) authentication.getPrincipal();

        String token = jwtTokenProvider.generateToken(authentication);

        // Corrected AuthResponse constructor call to include "Bearer" tokenType
        return new AuthResponse(
            token,
            "Bearer", // tokenType
            authenticatedUser.getId(),
            authenticatedUser.getEmail(),
            authenticatedUser.getRole().name()
        );
    }

    public AuthResponse registerAdmin(RegisterRequest registerRequest) {
        if (appUserRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already taken!");
        }

        AppUser admin = AppUser.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .contactNumber(registerRequest.getContactNumber())
                .role(AppUser.Role.ROLE_ADMIN) // Explicitly set admin role
                .createdDate(LocalDateTime.now())
                .lastUpdatedDate(LocalDateTime.now())
                .enabled(true)
                .build();

        appUserRepository.save(admin);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(registerRequest.getEmail(), registerRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);

        // Corrected AuthResponse constructor call to include "Bearer" tokenType
        return new AuthResponse(
            token,
            "Bearer", // tokenType
            admin.getId(),
            admin.getEmail(),
            admin.getRole().name()
        );
    }
}