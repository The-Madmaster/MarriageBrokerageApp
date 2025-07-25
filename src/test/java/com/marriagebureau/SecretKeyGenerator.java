package com.marriagebureau;

import io.jsonwebtoken.security.Keys;
import java.util.Base64;
import javax.crypto.SecretKey;
import io.jsonwebtoken.SignatureAlgorithm; // If you want to specify algorithm

public class SecretKeyGenerator {
    public static void main(String[] args) {
        // Generate a strong random key suitable for HS512 (512 bits = 64 bytes)
        // For HS256, you'd typically need 256 bits = 32 bytes.
        // JJWT's Keys.secretKeyFor() defaults to HS256 if no algorithm is specified
        // or can create based on the algorithm you pass. Let's create an HS512 key for robustness.
        SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

        // Encode the key to Base64
        String base64EncodedSecret = Base64.getEncoder().encodeToString(secretKey.getEncoded());

        System.out.println("Generated Base64 Encoded JWT Secret: " + base64EncodedSecret);
        System.out.println("--- IMPORTANT: Copy this string and paste it into application.properties ---");
        System.out.println("Make sure there are NO leading/trailing spaces or newlines.");
    }
}