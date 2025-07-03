    // src/main/java/com/marriagebureau/repository/UserRepository.java
    package com.marriagebureau.repository;

    import com.marriagebureau.user.User; // Import your User entity
    import org.springframework.data.jpa.repository.JpaRepository;
    import java.util.Optional;

    // This interface extends JpaRepository, providing CRUD operations for the User entity.
    // It also defines a custom method to find a User by their email.
    public interface UserRepository extends JpaRepository<User, Integer> {

        // Custom method to find a user by their email.
        // Spring Data JPA automatically generates the query for this method name.
        Optional<User> findByEmail(String email);
    }
    