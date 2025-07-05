package com.marriagebureau.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull; // Ensure this import is resolvable if you intend to use @NonNull
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService; // Use the interface

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils; // For StringUtils.hasText()
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    // We're injecting UserDetailsService here.
    // @Lazy is typically used to break circular dependencies.
    // Ensure your CustomUserDetailsService is correctly configured as a @Service
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        // Get JWT (token) from http request
        String token = getJwtFromRequest(request); // Reusing existing helper for clarity

        // Validate token and ensure userEmail is extracted
        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) { // Using validateToken from JwtTokenProvider
            // Get email from token using the correct method name
            String userEmail = jwtTokenProvider.getUserEmailFromJWT(token); // Using getUserEmailFromJWT

            // If userEmail is found and no authentication is currently set in the context
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Load user details using UserDetailsService
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                // Check if the token is valid for the loaded user (this check is often integrated into validateToken already)
                // The validateToken method already checks expiration etc. So a simple boolean check is enough.
                // However, if your isTokenValid method from original design also takes UserDetails, then use that.
                // For now, let's stick to the simple validateToken(token) and load user later.
                // If you want more granular checks (e.g., specific user details match claims), you'd expand this.

                // If token is valid, create an authentication object and set it in the SecurityContext
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // credentials are null because the token itself is the credential here
                        userDetails.getAuthorities()
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set Spring Security Context
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }

    // Helper method to extract JWT from Authorization header
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Extract token string after "Bearer "
        }
        return null;
    }
}