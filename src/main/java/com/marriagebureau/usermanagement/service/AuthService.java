package com.marriagebureau.usermanagement.service;

import com.marriagebureau.security.JwtTokenProvider;
import com.marriagebureau.usermanagement.dto.AuthResponse;
import com.marriagebureau.usermanagement.dto.LoginRequest;
import com.marriagebureau.usermanagement.dto.RegisterRequest;
import com.marriagebureau.usermanagement.exception.UserAlreadyExistsException;
import com.marriagebureau.usermanagement.model.AppUser;
import com.marriagebureau.usermanagement.model.Role;
import com.marriagebureau.usermanagement.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Registers a new broker, saves them to the database, and returns a JWT.
     */
    public AuthResponse register(RegisterRequest registerRequest) {
        if (appUserRepository.existsByEmail(registerRequest.getEmail())) {
            throw new UserAlreadyExistsException("Email '" + registerRequest.getEmail() + "' is already taken.");
        }

        AppUser user = AppUser.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .contactNumber(registerRequest.getContactNumber())
                .role(Role.ROLE_BROKER) // Default role for public registration
                .build();

        AppUser savedUser = appUserRepository.save(user);

        // Directly create the Authentication object for the new user to generate a token
        Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser, null, savedUser.getAuthorities());
        String accessToken = jwtTokenProvider.generateToken(authentication);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .build();
    }

    /**
     * Authenticates an existing user and returns a JWT.
     */
    public AuthResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        
        String accessToken = jwtTokenProvider.generateToken(authentication);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .build();
    }
}