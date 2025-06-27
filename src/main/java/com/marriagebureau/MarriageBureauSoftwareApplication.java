// MarriageBureauSoftwareApplication.java
package com.marriagebureau; // Make sure this matches your base package

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication // This is the main annotation that kicks off Spring Boot
public class MarriageBureauSoftwareApplication {

    public static void main(String[] args) {
        // This is the main method that starts your Spring Boot application
        SpringApplication.run(MarriageBureauSoftwareApplication.class, args);
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}