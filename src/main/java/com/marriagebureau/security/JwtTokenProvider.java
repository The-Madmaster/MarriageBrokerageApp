package com.marriagebureau.security;

import com.marriagebureau.usermanagement.entity.AppUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

// import java.security.Key; // No longer strictly needed if SecretKey is always used
import javax.crypto.SecretKey; // **NEW: This import is crucial**
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwt-secret}") // Get secret from application.properties
    private String jwtSecret;

    @Value("${app.jwt-expiration-milliseconds}") // Get expiration from application.properties
    private long jwtExpirationDate;

    // Helper method to get the signing key.
    // It's crucial to generate the key once and reuse it across operations.
    // This is the correct way to get the Key for JJWT 0.12.x+
    private SecretKey key() { // Changed return type to SecretKey
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    // Method to generate token
    public String generateToken(Authentication authentication) {
        // Ensure that authentication.getPrincipal() correctly returns your AppUser
        // If you are using CustomUserDetailsService, it often returns User (Spring Security's User)
        // If your CustomUserDetailsService returns AppUser directly, this cast is fine.
        // Otherwise, you might need an adapter like UserPrincipal or cast to User and get email.
        AppUser userPrincipal = (AppUser) authentication.getPrincipal();

        String userEmail = userPrincipal.getEmail(); // Subject of the token
        Long userId = userPrincipal.getId(); // Custom claim for userId

        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        String token = Jwts.builder()
                .subject(userEmail)
                .claim("userId", userId) // Add userId as a custom claim
                .issuedAt(currentDate)
                .expiration(expireDate)
                .signWith(key()) // Use the generated Key directly, algorithm inferred from Key
                .compact();
        return token;
    }

    // Extracts the user email (subject) from the JWT token.
    public String getUserEmailFromJWT(String token) {
        // Use Jwts.parser() with .verifyWith(Key) for validation
        Claims claims = Jwts.parser()
                .verifyWith(key()) // Uses SecretKey now, which matches verifyWith(SecretKey)
                .build() // Build the parser instance
                .parseSignedClaims(token) // Use parseSignedClaims() instead of parseClaimsJws()
                .getPayload(); // Use getPayload() instead of getBody()
        return claims.getSubject();
    }

    // Validates the JWT token.
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key()) // Uses SecretKey now, which matches verifyWith(SecretKey)
                    .build() // Build the parser instance
                    .parseSignedClaims(token); // Use parseSignedClaims() instead of parseClaimsJws()
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token: {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty: {}", ex.getMessage());
        }
        return false;
    }

    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key()) // Uses SecretKey now, which matches verifyWith(SecretKey)
                .build() // Build the parser instance
                .parseSignedClaims(token) // Use parseSignedClaims() instead of parseClaimsJws()
                .getPayload(); // Use getPayload() instead of getBody()

        // Claims.get() returns an Object. If it's a number, it's typically Integer or Long.
        // It's safer to cast to Number and then call longValue() to avoid ClassCastException.
        return ((Number) claims.get("userId")).longValue();
    }
}