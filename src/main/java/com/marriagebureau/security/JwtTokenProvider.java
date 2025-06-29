package com.marriagebureau.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app.jwt-expiration-milliseconds}")
    private long jwtExpirationDate; // Use long for milliseconds

    // Get JWT Secret Key
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    // Generate JWT token
    public String generateToken(Authentication authentication) {
        String username = authentication.getName(); // Get username from authenticated user

        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(key(), SignatureAlgorithm.HS512) // Use HS512 algorithm
                .compact();

        return token;
    }

    // Get username from JWT token
    public String getUsername(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // Validate JWT token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parse(token); // Use .parse() for general parsing and validation
            return true;
        } catch (SignatureException ex) {
            // Invalid JWT signature
            // Log this exception: logger.error("Invalid JWT signature");
            System.out.println("Invalid JWT signature"); // For development
        } catch (MalformedJwtException ex) {
            // Invalid JWT token
            // Log this exception: logger.error("Invalid JWT token");
            System.out.println("Invalid JWT token"); // For development
        } catch (ExpiredJwtException ex) {
            // Expired JWT token
            // Log this exception: logger.error("Expired JWT token");
            System.out.println("Expired JWT token"); // For development
        } catch (UnsupportedJwtException ex) {
            // Unsupported JWT token
            // Log this exception: logger.error("Unsupported JWT token");
            System.out.println("Unsupported JWT token"); // For development
        } catch (IllegalArgumentException ex) {
            // JWT claims string is empty
            // Log this exception: logger.error("JWT claims string is empty");
            System.out.println("JWT claims string is empty"); // For development
        }
        return false;
    }
}