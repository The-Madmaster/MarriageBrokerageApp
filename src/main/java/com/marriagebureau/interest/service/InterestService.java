package com.marriagebureau.interest.service;

import com.marriagebureau.clientmanagement.mapper.ProfileMapper;
import com.marriagebureau.clientmanagement.model.Profile;
import com.marriagebureau.clientmanagement.repository.ProfileRepository;
import com.marriagebureau.interest.dto.InterestRequestDTO;
import com.marriagebureau.interest.dto.InterestResponseDTO;
import com.marriagebureau.interest.dto.InterestUpdateDTO;
import com.marriagebureau.interest.model.Interest;
import com.marriagebureau.interest.model.InterestStatus;
import com.marriagebureau.interest.repository.InterestRepository;
import com.marriagebureau.security.SecurityService;
import com.marriagebureau.usermanagement.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterestService {

    private final InterestRepository interestRepository;
    private final ProfileRepository profileRepository;
    private final SecurityService securityService;

    @Transactional
    public InterestResponseDTO sendInterest(InterestRequestDTO request) {
        Long currentBrokerId = securityService.getCurrentUserId();
        Profile senderProfile = profileRepository.findById(request.getSenderProfileId())
                .orElseThrow(() -> new ResourceNotFoundException("Sender profile not found with ID: " + request.getSenderProfileId()));
        Profile receiverProfile = profileRepository.findById(request.getReceiverProfileId())
                .orElseThrow(() -> new ResourceNotFoundException("Receiver profile not found with ID: " + request.getReceiverProfileId()));

        if (!senderProfile.getBroker().getId().equals(currentBrokerId)) {
            throw new AccessDeniedException("You can only send interests from your own client profiles.");
        }
        if (senderProfile.getBroker().getId().equals(receiverProfile.getBroker().getId())) {
            throw new IllegalArgumentException("You cannot send an interest to a client managed by the same broker.");
        }
        if (interestRepository.existsBySenderProfileAndReceiverProfile(senderProfile, receiverProfile)) {
            throw new IllegalArgumentException("An interest request has already been sent to this profile.");
        }

        Interest newInterest = Interest.builder()
                .senderProfile(senderProfile)
                .receiverProfile(receiverProfile)
                .build(); // Status and sentAt are set by @PrePersist in the entity

        Interest savedInterest = interestRepository.save(newInterest);
        return mapInterestToResponseDTO(savedInterest);
    }

    @Transactional(readOnly = true)
    public List<InterestResponseDTO> getSentInterests(Long senderProfileId) {
        Long currentBrokerId = securityService.getCurrentUserId();
        Profile senderProfile = profileRepository.findById(senderProfileId)
            .orElseThrow(() -> new ResourceNotFoundException("Profile not found with ID: " + senderProfileId));
        if (!senderProfile.getBroker().getId().equals(currentBrokerId)) {
            throw new AccessDeniedException("You can only view interests for your own clients.");
        }

        return interestRepository.findBySenderProfileId(senderProfileId).stream()
                .map(this::mapInterestToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InterestResponseDTO> getReceivedInterests(Long receiverProfileId) {
        Long currentBrokerId = securityService.getCurrentUserId();
        Profile receiverProfile = profileRepository.findById(receiverProfileId)
            .orElseThrow(() -> new ResourceNotFoundException("Profile not found with ID: " + receiverProfileId));
        if (!receiverProfile.getBroker().getId().equals(currentBrokerId)) {
            throw new AccessDeniedException("You can only view interests for your own clients.");
        }

        return interestRepository.findByReceiverProfileId(receiverProfileId).stream()
                .map(this::mapInterestToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public InterestResponseDTO respondToInterest(Long interestId, InterestUpdateDTO updateDTO) {
        Long currentBrokerId = securityService.getCurrentUserId();
        Interest interest = interestRepository.findById(interestId)
                .orElseThrow(() -> new ResourceNotFoundException("Interest request not found with ID: " + interestId));

        if (!interest.getReceiverProfile().getBroker().getId().equals(currentBrokerId)) {
            throw new AccessDeniedException("You can only respond to interests received by your clients.");
        }
        if (interest.getStatus() != InterestStatus.PENDING) {
            throw new IllegalStateException("This interest request has already been responded to.");
        }

        try {
            InterestStatus newStatus = InterestStatus.valueOf(updateDTO.getStatus().toUpperCase());
            if (newStatus == InterestStatus.PENDING) {
                throw new IllegalArgumentException("Cannot set status back to PENDING.");
            }
            interest.setStatus(newStatus);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status provided. Must be 'ACCEPTED' or 'REJECTED'.");
        }
        
        Interest updatedInterest = interestRepository.save(interest);
        return mapInterestToResponseDTO(updatedInterest);
    }

    private InterestResponseDTO mapInterestToResponseDTO(Interest interest) {
        return InterestResponseDTO.builder()
                .id(interest.getId())
                .senderProfile(ProfileMapper.toProfileResponse(interest.getSenderProfile()))
                .receiverProfile(ProfileMapper.toProfileResponse(interest.getReceiverProfile()))
                .status(interest.getStatus().name())
                .sentAt(interest.getSentAt())
                .build();
    }
}