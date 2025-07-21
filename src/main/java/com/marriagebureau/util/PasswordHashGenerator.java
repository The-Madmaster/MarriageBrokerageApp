package com.marriagebureau.util; // Or com.marriagebureau.security.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {

    public static void main(String[] args) {
        // The password you want to hash (e.g., the plain-text password for admin@example.com)
        String rawPassword = "adminpassword";
        // You can change this to hash other passwords as needed
        // String rawPassword = "anotherpassword123";

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(rawPassword);

        System.out.println("----------------------------------------------------");
        System.out.println("Encoded password for '" + rawPassword + "':");
        System.out.println(encodedPassword);
        System.out.println("----------------------------------------------------");
    }
}