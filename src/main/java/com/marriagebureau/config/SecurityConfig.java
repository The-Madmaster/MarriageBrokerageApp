// src/main/java/com/marriagebureau/config/SecurityConfig.java

package com.marriagebureau.config; // Adjust package name if yours is different

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.config.Customizer; // Needed for newer Spring Security versions

@Configuration
@EnableWebSecurity // Enables Spring Security's web security support and provides the Spring Security integration.
public class SecurityConfig {

    /**
     * Configures the security filter chain.
     * This method defines authorization rules, CSRF protection, etc.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF protection.
            // CSRF is usually important for web applications with session-based authentication
            // but can be safely disabled for stateless REST APIs using token-based authentication (like JWT).
            .csrf(csrf -> csrf.disable())

            // Configure authorization rules for HTTP requests.
            .authorizeHttpRequests(authorize -> authorize
                // Allow all requests to any path without authentication.
                // THIS IS FOR DEVELOPMENT/TESTING ONLY!
                .anyRequest().permitAll()
            )
            // Optional: If you want to explicitly enable form login (though it won't be triggered with permitAll())
            // .formLogin(Customizer.withDefaults())
            // Optional: If you want to explicitly enable HTTP Basic authentication
            // .httpBasic(Customizer.withDefaults())
            ;

        return http.build();
    }

    /**
     * Defines a PasswordEncoder bean for encoding passwords.
     * BCrypt is a strong, standard hashing algorithm for passwords.
     * Even if not explicitly used with permitAll(), it's a good practice to define it.
     */
    //@Bean
    //public PasswordEncoder passwordEncoder() //{
     //   return new BCryptPasswordEncoder();
    //}

    /**
     * Provides an in-memory user details manager.
     * This is a placeholder and primarily useful if your application
     * implicitly expects a UserDetailsService bean to be present,
     * even when using permitAll().
     * For actual authentication, you would replace this with a service
     * that loads users from a database or other source.
     */
    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        // You can define a dummy user here if needed, but it won't affect permitAll()
        // UserDetails user = User.builder()
        //     .username("user")
        //     .password(passwordEncoder.encode("password"))
        //     .roles("USER")
        //     .build();
        // UserDetails admin = User.builder()
        //     .username("admin")
        //     .password(passwordEncoder.encode("admin"))
        //     .roles("ADMIN", "USER")
        //     .build();
        // return new InMemoryUserDetailsManager(user, admin);
        return new InMemoryUserDetailsManager(); // An empty manager is fine if permitAll()
    }
}