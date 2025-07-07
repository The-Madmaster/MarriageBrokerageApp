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

import javax.crypto.SecretKey; // This is the correct import for SecretKey
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
    private SecretKey key() { // **Changed return type from 'Key' to 'SecretKey'**
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    // Method to generate token
    public String generateToken(Authentication authentication) {
        AppUser userPrincipal = (AppUser) authentication.getPrincipal(); // Cast to your AppUser

        String userEmail = userPrincipal.getEmail(); // Subject of the token
        Long userId = userPrincipal.getId(); // Custom claim for userId

        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        String token = Jwts.builder()
                .subject(userEmail) // Using modern fluent API: .subject() instead of .setSubject()
                .claim("userId", userId) // Add userId as a custom claim
                .issuedAt(currentDate) // Using modern fluent API: .issuedAt() instead of .setIssuedAt()
                .expiration(expireDate) // Using modern fluent API: .expiration() instead of .setExpiration()
                .signWith(key()) // Using modern fluent API: .signWith(Key) directly, HS384 inferred
                .compact();
        return token;
    }

    // Extracts the user email (subject) from the JWT token.
    public String getUserEmailFromJWT(String token) {
        Claims claims = Jwts.parser() // Correct: use Jwts.parser() for modern API
                .verifyWith(key())   // Correct: use verifyWith() instead of setSigningKey()
                .build()
                .parseSignedClaims(token) // Correct: use parseSignedClaims()
                .getPayload(); // Get the payload to access claims
        return claims.getSubject();
    }

    // Validates the JWT token.
    public boolean validateToken(String token) {
        try {
            Jwts.parser() // Correct: use Jwts.parser() for modern API
                    .verifyWith(key()) // Correct: use verifyWith() instead of setSigningKey()
                    .build()
                    .parseSignedClaims(token); // Correct: use parseSignedClaims()
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

    // Extracts userId from JWT.
    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser() // Correct: use Jwts.parser() for modern API
                .verifyWith(key())   // Correct: use verifyWith() instead of setSigningKey()
                .build()
                .parseSignedClaims(token) // Correct: use parseSignedClaims()
                .getPayload(); // Get the payload to access claims
        return ((Number) claims.get("userId")).longValue();
    }
}