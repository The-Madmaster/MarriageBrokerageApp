// UserRepository.java
package com.marriagebureau.usermanagement.repository;

import java.util.Optional; // Import your User model

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.marriagebureau.entity.User; // Import Optional for methods that might not find a result


@Repository // Marks this interface as a Spring Data JPA repository
public interface UserRepository extends JpaRepository<User, Long> {
    // JpaRepository provides standard CRUD operations like save(), findById(), findAll(), delete()

    // Custom method to find a user by their email. Spring Data JPA automatically implements this!
  //./mvnw clean install -DskipTests  Optional<User> findByEmail(String email);
   
    

    Optional<User> findByUsername(String username);
    // Custom method to find a user by their contact number
    Optional<User> findByContactNumber(String contactNumber);
}