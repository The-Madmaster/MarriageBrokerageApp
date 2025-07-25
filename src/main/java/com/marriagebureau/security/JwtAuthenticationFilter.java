package com.marriagebureau.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtTokenProvider jwtTokenProvider;

    // @Lazy is okay here, but if you don't have a circular dependency, it's not strictly necessary.
    // It can sometimes mask configuration issues if used unnecessarily.
    // However, for typical Spring Security setups with JwtAuthenticationFilter and CustomUserDetailsService, it's fine.
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String token = getJwtFromRequest(request);

        // Add debug log to see if token is extracted and if validation is attempted
        logger.debug("Attempting to authenticate request for URI: {}", request.getRequestURI());
        logger.debug("Extracted JWT: {}", StringUtils.hasText(token) ? "Present" : "Not Present");


        // Validate token and ensure userEmail is extracted
        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            String userEmail = jwtTokenProvider.getUserEmailFromJWT(token);

            // If userEmail is found and no authentication is currently set in the context
            // The SecurityContextHolder check prevents overwriting an existing authentication (e.g., from other filters)
            if (StringUtils.hasText(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null) {
                logger.debug("JWT is valid. Loading UserDetails for email: {}", userEmail);
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                // Additional check: Ensure userDetails is not null and is enabled (though AppUser.isEnabled() should handle this)
                if (userDetails != null && userDetails.isEnabled() && userDetails.isAccountNonExpired() && userDetails.isAccountNonLocked() && userDetails.isCredentialsNonExpired()) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null, // credentials are null as they were already validated via JWT
                            userDetails.getAuthorities()
                    );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.debug("Successfully set authentication in SecurityContext for user: {}", userDetails.getUsername());
                    // ⭐ The corrected and added crucial debug logs ⭐
                    logger.debug("--- JWT Filter: SecurityContextHolder State AFTER setting authentication ---");
                    logger.debug("Authentication Object (in JWT Filter): {}", SecurityContextHolder.getContext().getAuthentication());
                    logger.debug("Authentication Principal (in JWT Filter): {}", SecurityContextHolder.getContext().getAuthentication() != null ? SecurityContextHolder.getContext().getAuthentication().getPrincipal() : "NULL");
                    logger.debug("Is Authenticated (in JWT Filter): {}", SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
                    logger.debug("--- END JWT Filter: SecurityContextHolder State ---");
                } else {
                    logger.warn("User details for {} are invalid (e.g., disabled, locked, expired). Not authenticating.", userEmail);
                }
            } else {
                 if (StringUtils.hasText(userEmail)) {
                    logger.debug("User email {} extracted, but SecurityContext already has authentication.", userEmail);
                 } else {
                    logger.debug("User email not extracted from token or is empty.");
                 }
            }
        } else {
            logger.debug("JWT is either not present, empty, or invalid for URI: {}", request.getRequestURI());
            // This path is expected for unauthenticated requests or invalid tokens.
            // Spring Security's other filters (like AuthorizationFilter) will handle the 401 if needed.
        }

        // Pass the request to the next filter in the chain
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