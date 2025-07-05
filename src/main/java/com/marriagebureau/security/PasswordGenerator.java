package com.marriagebureau.security; // Adjust package if you placed it elsewhere

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "password"; // <-- Set the plain text password you want to use for 'admin'
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("Encoded password for '" + rawPassword + "': " + encodedPassword);
    }
}