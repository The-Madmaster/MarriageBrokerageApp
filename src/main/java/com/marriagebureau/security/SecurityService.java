package com.marriagebureau.security;

import com.marriagebureau.usermanagement.entity.Profile;
import com.marriagebureau.usermanagement.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("securityService")
public class SecurityService {

    private final ProfileService profileService;

    @Autowired
    public SecurityService(ProfileService profileService) {
        this.profileService = profileService;
    }

    public boolean isOwner(Long profileId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() instanceof String && "anonymousUser".equals(authentication.getPrincipal())) {
            return false;
        }

        // Ensure the principal is of type UserPrincipal to access getUserId()
        if (!(authentication.getPrincipal() instanceof UserPrincipal)) {
            System.err.println("Authenticated principal is not of type UserPrincipal: " + authentication.getPrincipal().getClass().getName());
            return false;
        }

        Long authenticatedUserId = ((UserPrincipal) authentication.getPrincipal()).getUserId();

        Optional<Profile> profileOptional = profileService.getProfileById(profileId);

        // Access the AppUser associated with the profile and compare its ID.
        // Lombok should generate getAppUser() for Profile.
        return profileOptional.map(profile -> profile.getAppUser().getId().equals(authenticatedUserId)).orElse(false);
    }
}