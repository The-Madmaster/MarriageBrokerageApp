package com.marriagebureau.clientmanagement.service;

import com.marriagebureau.clientmanagement.dto.CreateProfileRequest;
import com.marriagebureau.clientmanagement.dto.ProfileResponse;
import com.marriagebureau.clientmanagement.dto.ProfileSearchRequest;
import com.marriagebureau.clientmanagement.dto.UpdateProfileRequest;
import com.marriagebureau.clientmanagement.mapper.ProfileMapper;
import com.marriagebureau.clientmanagement.model.Profile;
import com.marriagebureau.clientmanagement.repository.ProfileRepository;
import com.marriagebureau.usermanagement.exception.ResourceNotFoundException;
import com.marriagebureau.usermanagement.model.AppUser;
import com.marriagebureau.usermanagement.repository.AppUserRepository;
import com.marriagebureau.security.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Import the static methods from your specifications class
import static com.marriagebureau.clientmanagement.service.ProfileSpecifications.*;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final AppUserRepository appUserRepository;
    private final SecurityService securityService;

    @Transactional
    public ProfileResponse createProfile(CreateProfileRequest request) {
        AppUser broker = getAuthenticatedBroker();

        Profile profile = Profile.builder()
                .broker(broker)
                .fullName(request.getFullName())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .maritalStatus(request.getMaritalStatus())
                .heightCm(request.getHeightCm())
                .religion(request.getReligion())
                .caste(request.getCaste())
                .subCaste(request.getSubCaste())
                .motherTongue(request.getMotherTongue())
                .country(request.getCountry())
                .state(request.getState())
                .city(request.getCity())
                .education(request.getEducation())
                .occupation(request.getOccupation())
                .annualIncome(request.getAnnualIncome())
                .photoUrl(request.getPhotoUrl())
                .isActive(true)
                .build();

        Profile savedProfile = profileRepository.save(profile);
        return ProfileMapper.toProfileResponse(savedProfile);
    }

    @Transactional(readOnly = true)
    public List<ProfileResponse> getClientsForAuthenticatedBroker() {
        AppUser broker = getAuthenticatedBroker();
        return profileRepository.findAllByBroker(broker)
                .stream()
                .map(ProfileMapper::toProfileResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProfileResponse getProfileByIdForBroker(Long profileId) {
        Profile profile = getProfileAndVerifyOwnership(profileId);
        return ProfileMapper.toProfileResponse(profile);
    }

    @Transactional
    public ProfileResponse updateProfile(Long profileId, UpdateProfileRequest request) {
        Profile profile = getProfileAndVerifyOwnership(profileId);
        Optional.ofNullable(request.getFullName()).ifPresent(profile::setFullName);
        Optional.ofNullable(request.getDateOfBirth()).ifPresent(profile::setDateOfBirth);
        Optional.ofNullable(request.getIsActive()).ifPresent(profile::setActive);

        Profile updatedProfile = profileRepository.save(profile);
        return ProfileMapper.toProfileResponse(updatedProfile);
    }

    @Transactional
    public void deleteProfile(Long profileId) {
        Profile profile = getProfileAndVerifyOwnership(profileId);
        profileRepository.delete(profile);
    }

    @Transactional(readOnly = true)
    public Page<ProfileResponse> searchProfiles(ProfileSearchRequest request) {
        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.Direction.fromString(request.getSortDirection()),
                request.getSortBy()
        );

        Specification<Profile> spec = Specification.where(isActive())
                .and(hasMinAge(request.getMinAge()))
                .and(hasMaxAge(request.getMaxAge()))
                .and(hasGender(request.getGender()))
                .and(hasCountry(request.getCountry()))
                .and(hasReligion(request.getReligion()));
        
        Page<Profile> results = profileRepository.findAll(spec, pageable);
        return results.map(ProfileMapper::toProfileResponse);
    }

    private Profile getProfileAndVerifyOwnership(Long profileId) {
        Long currentBrokerId = securityService.getCurrentUserId();
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found with ID: " + profileId));

        if (!profile.getBroker().getId().equals(currentBrokerId)) {
            throw new AccessDeniedException("You do not have permission to access this profile.");
        }
        return profile;
    }
    
    private AppUser getAuthenticatedBroker() {
        Long currentBrokerId = securityService.getCurrentUserId();
        if (currentBrokerId == null) {
            throw new AccessDeniedException("No authenticated user found.");
        }
        return appUserRepository.findById(currentBrokerId)
            .orElseThrow(() -> new ResourceNotFoundException("Authenticated broker not found with ID: " + currentBrokerId));
    }
}