    // src/main/java/com/marriagebureau/user/User.java
    package com.marriagebureau.user;

    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetails;

    import java.util.Collection;
    import java.util.List;

    @Data // Lombok: Generates getters, setters, toString, equals, and hashCode
    @Builder // Lombok: Provides a builder pattern for object creation
    @NoArgsConstructor // Lombok: Generates a no-argument constructor
    @AllArgsConstructor // Lombok: Generates a constructor with all fields
    @Entity // Marks this class as a JPA entity
    @Table(name = "_user") // Specifies the database table name (avoiding 'user' keyword in some DBs)
    public class User implements UserDetails {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrementing ID
        private Integer id;
        private String firstname;
        private String lastname;
        private String email;
        private String password;

        @Enumerated(EnumType.STRING) // Stores the enum as a string in the database
        private Role role;

        // --- UserDetails Interface Implementations ---

        // Returns the authorities (roles) granted to the user.
        // In this case, it's a list containing a single SimpleGrantedAuthority based on the user's role.
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return List.of(new SimpleGrantedAuthority(role.name()));
        }

        // Returns the password used to authenticate the user.
        @Override
        public String getPassword() {
            return password;
        }

        // Returns the username used to authenticate the user.
        // We're using email as the username.
        @Override
        public String getUsername() {
            return email;
        }

        // Indicates whether the user's account has expired.
        // True indicates that the user's account is valid (i.e., non-expired).
        @Override
        public boolean isAccountNonExpired() {
            return true; // For simplicity, setting to true. In a real app, manage account expiration.
        }

        // Indicates whether the user is locked or unlocked.
        // True indicates that the user is not locked.
        @Override
        public boolean isAccountNonLocked() {
            return true; // For simplicity, setting to true. In a real app, manage account locking.
        }

        // Indicates whether the user's credentials (password) have expired.
        // True indicates that the user's credentials are valid (i.e., non-expired).
        @Override
        public boolean isCredentialsNonExpired() {
            return true; // For simplicity, setting to true. In a real app, manage credential expiration.
        }

        // Indicates whether the user is enabled or disabled.
        // True indicates that the user is enabled.
        @Override
        public boolean isEnabled() {
            return true; // For simplicity, setting to true. In a real app, manage user enablement.
        }
    }
    