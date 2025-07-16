package com.marriagebureau.security.jwt;

import com.marriagebureau.usermanagement.entity.AppUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // Secret key for JWT signing, loaded from application.properties
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    // JWT expiration in milliseconds, loaded from application.properties
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    /**
     * Extracts the username (email) from the JWT token.
     * @param token The JWT token.
     * @return The username (email) contained in the token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a specific claim from the JWT token using a resolver function.
     * @param token The JWT token.
     * @param claimsResolver A function to resolve the desired claim from the Claims.
     * @param <T> The type of the claim.
     * @return The extracted claim.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Generates a JWT token for a given UserDetails (AppUser).
     * @param userDetails The UserDetails object representing the authenticated user.
     * @return The generated JWT token.
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Generates a JWT token with extra claims.
     * This method is updated to use the modern JJWT API.
     * @param extraClaims Additional claims to include in the token.
     * @param userDetails The UserDetails object representing the authenticated user.
     * @return The generated JWT token.
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        // Cast UserDetails to AppUser to access specific fields like ID and Role
        AppUser appUser = (AppUser) userDetails;

        // Add userId and role to claims as discussed
        // Using `addClaims` for additional claims (JJWT 0.12.x style)
        // or directly setting them as a Map using `claims()` builder method.
        // For simplicity and directness, we'll build a map and use `claims()`.
        Map<String, Object> allClaims = new HashMap<>(extraClaims);
        allClaims.put("userId", appUser.getId());
        allClaims.put("role", appUser.getRole().name()); // Assuming getRole() returns an enum with a .name() method

        return Jwts.builder()
                .claims(allClaims) // Use claims() to set all claims at once
                .subject(userDetails.getUsername()) // Use subject() instead of setSubject()
                .issuedAt(new Date(System.currentTimeMillis())) // Use issuedAt() instead of setIssuedAt()
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration)) // Use expiration() instead of setExpiration()
                .signWith(getSignInKey()) // Use signWith(Key) directly
                .compact();
    }

    /**
     * Validates a JWT token against UserDetails.
     * @param token The JWT token to validate.
     * @param userDetails The UserDetails object to compare against.
     * @return True if the token is valid, false otherwise.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Checks if the JWT token is expired.
     * @param token The JWT token.
     * @return True if the token is expired, false otherwise.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date from the JWT token.
     * @param token The JWT token.
     * @return The expiration Date.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts all claims from the JWT token.
     * This method is correct for JJWT 0.11.0+ and 0.12.x.
     * @param token The JWT token.
     * @return The Claims object containing all claims.
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Decodes the secret key and returns it as a Key object.
     * @return The signing Key.
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}