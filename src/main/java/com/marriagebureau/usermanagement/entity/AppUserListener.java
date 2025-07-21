// src/main/java/com/marriagebureau/usermanagement/entity/AppUserListener.java
package com.marriagebureau.usermanagement.entity;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;

public class AppUserListener {

    @PrePersist
    public void prePersist(AppUser appUser) {
        appUser.setCreatedDate(LocalDateTime.now());
        appUser.setLastUpdatedDate(LocalDateTime.now());
    }

    @PreUpdate
    public void preUpdate(AppUser appUser) {
        appUser.setLastUpdatedDate(LocalDateTime.now());
    }
}