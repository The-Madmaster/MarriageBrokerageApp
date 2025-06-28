// src/main/java/com/marriagebureau/entity/User.java
package com.marriagebureau.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "user") // Using 'user' as the table name
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(length = 20)
    private String contactNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role; // Using an enum for roles

    // Potentially add other fields if needed, e.g., createdDate, lastLogin, etc.
}