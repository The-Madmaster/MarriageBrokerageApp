// src/main/java/com/marriagebureau/usermanagement/model/Role.java
package com.marriagebureau.usermanagement.model;

// Defines the roles a user can have in the application, following Spring Security conventions.
public enum Role {
    ROLE_BROKER, // Primary user type for this application
    ROLE_ADMIN   // Administrative user
}