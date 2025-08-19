package com.marriagebureau.matches.service;

import com.marriagebureau.clientmanagement.mapper.ProfileMapper;
import com.marriagebureau.clientmanagement.model.Profile;
import com.marriagebureau.clientmanagement.model.enums.Gender;
import com.marriagebureau.clientmanagement.model.enums.MaritalStatus;
import com.marriagebureau.clientmanagement.repository.ProfileRepository;
import com.marriagebureau.matches.dto.MatchResultDto;
import com.marriagebureau.usermanagement.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.marriagebureau.clientmanagement.service.ProfileSpecifications.*;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final ProfileRepository profileRepository;

    @Transactional(readOnly = true)
    public List<MatchResultDto> findMatchesForProfile(Long requestingProfileId) {
        // 1. Get the profile of the person we are finding matches for.
        Profile requestingProfile = profileRepository.findById(requestingProfileId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found with ID: " + requestingProfileId));

        // 2. Determine the target gender for the search.
        Gender targetGender = (requestingProfile.getGender() == Gender.MALE) ? Gender.FEMALE : Gender.MALE;

        // 3. Build a dynamic query using specifications based on the client's preferences.
        Specification<Profile> spec = Specification.where(isActive())
                .and(isNotProfile(requestingProfileId)) // Don't match with self
                .and(hasGender(targetGender))
                .and(isWithinAgeRange(requestingProfile.getPreferredPartnerMinAge(), requestingProfile.getPreferredPartnerMaxAge()))
                .and(isWithinHeightRange(requestingProfile.getPreferredPartnerMinHeightCm(), requestingProfile.getPreferredPartnerMaxHeightCm()))
                .and(hasReligion(requestingProfile.getPreferredPartnerReligion()))
                .and(hasCaste(requestingProfile.getPreferredPartnerCaste()))
                .and(hasMaritalStatus(MaritalStatus.NEVER_MARRIED)); // Example: only find single people

        // 4. Execute the efficient database query.
        List<Profile> potentialMatches = profileRepository.findAll(spec);

        // 5. Convert the results into MatchResultDto objects.
        // (Here we can add more complex "soft" scoring logic in the future).
        return potentialMatches.stream()
                .map(matchedProfile -> MatchResultDto.builder()
                        .matchedProfile(ProfileMapper.toProfileResponse(matchedProfile))
                        .compatibilityScore(100.0) // For now, anyone who passes the hard filters is a 100% match.
                        .build())
                .collect(Collectors.toList());
    }
}