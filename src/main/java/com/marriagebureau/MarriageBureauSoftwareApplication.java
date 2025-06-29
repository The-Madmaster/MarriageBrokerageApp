package com.marriagebureau;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean; // <--- ADD THIS IMPORT
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // <--- ADD THIS IMPORT
import org.springframework.security.crypto.password.PasswordEncoder; // <--- ADD THIS IMPORT

@SpringBootApplication
public class MarriageBureauSoftwareApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarriageBureauSoftwareApplication.class, args);
    }

    // Add this bean for password encoding
   // @Bean
    //public PasswordEncoder passwordEncoder() {
    //    return new BCryptPasswordEncoder();
    //}
}