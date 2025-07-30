package com.marriagebureau.interest.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.marriagebureau.interest.dto.InterestRequestDTO;
import com.marriagebureau.interest.dto.InterestResponseDTO;
import com.marriagebureau.interest.dto.InterestUpdateDTO;
import com.marriagebureau.interest.service.InterestService;

import java.util.List;

@RestController
@RequestMapping("/api/interests")
@PreAuthorize("hasRole('BROKER')") // Secures all endpoints for brokers
@RequiredArgsConstructor
public class InterestController {

    private final InterestService interestService;

    /**
     * Sends an interest request from one client to another.
     */
    @PostMapping("/send")
    public ResponseEntity<InterestResponseDTO> sendInterest(@Valid @RequestBody InterestRequestDTO request) {
        InterestResponseDTO response = interestService.sendInterest(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Gets all interests sent by a specific client profile.
     * The service layer will verify that the broker owns this profile.
     */
    @GetMapping("/sent/{profileId}")
    public ResponseEntity<List<InterestResponseDTO>> getSentInterests(@PathVariable Long profileId) {
        List<InterestResponseDTO> interests = interestService.getSentInterests(profileId);
        return ResponseEntity.ok(interests);
    }

    /**
     * Gets all interests received by a specific client profile.
     * The service layer will verify that the broker owns this profile.
     */
    @GetMapping("/received/{profileId}")
    public ResponseEntity<List<InterestResponseDTO>> getReceivedInterests(@PathVariable Long profileId) {
        List<InterestResponseDTO> interests = interestService.getReceivedInterests(profileId);
        return ResponseEntity.ok(interests);
    }

    /**
     * Responds to a pending interest request (accepts or rejects it).
     * The service layer will verify that the broker owns the receiving profile.
     */
    @PutMapping("/respond/{interestId}")
    public ResponseEntity<InterestResponseDTO> respondToInterest(@PathVariable Long interestId, @Valid @RequestBody InterestUpdateDTO updateDTO) {
        InterestResponseDTO response = interestService.respondToInterest(interestId, updateDTO);
        return ResponseEntity.ok(response);
    }
}