package com.marriagebureau.clientmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marriagebureau.clientmanagement.dto.CreateProfileRequest;
import com.marriagebureau.clientmanagement.dto.UpdateProfileRequest;
import com.marriagebureau.clientmanagement.model.enums.Gender;
import com.marriagebureau.clientmanagement.model.enums.MaritalStatus;
import com.marriagebureau.clientmanagement.model.enums.MotherTongue;
import com.marriagebureau.usermanagement.dto.AuthResponse;
import com.marriagebureau.usermanagement.dto.RegisterRequest;
import com.marriagebureau.usermanagement.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Ensures each test runs in a clean database transaction
class ClientProfileControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthService authService;

    private String brokerToken;

    // This method runs before each test, setting up an authenticated broker
    @BeforeEach
    void setup() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("crud.test.broker@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setContactNumber("9988776655");

        AuthResponse authResponse = authService.register(registerRequest);
        this.brokerToken = authResponse.getAccessToken();
    }

   // In src/test/java/com/marriagebureau/clientmanagement/controller/ClientProfileControllerIntegrationTest.java

    @Test
    void testClientProfileCrudFlow() throws Exception {
        // 1. CREATE
        CreateProfileRequest createRequest = new CreateProfileRequest();
        createRequest.setFullName("Arjun Sharma");
        createRequest.setDateOfBirth(LocalDate.of(1995, 5, 20));
        // --- ADDED ALL MANDATORY FIELDS ---
        createRequest.setGender(Gender.MALE);
        createRequest.setMaritalStatus(MaritalStatus.NEVER_MARRIED);
        createRequest.setHeightCm(180);
        createRequest.setReligion("Hindu");
        createRequest.setCaste("Brahmin");
        createRequest.setMotherTongue(MotherTongue.HINDI);
        createRequest.setCountry("India");
        createRequest.setState("Maharashtra");
        createRequest.setCity("Pune");
        createRequest.setEducation("M.Tech");
        createRequest.setOccupation("Software Engineer");
        createRequest.setAnnualIncome(2500000.0);
        
        MvcResult createResult = mockMvc.perform(post("/api/clients")
                .header("Authorization", "Bearer " + brokerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated()) // We now expect 201 Created
                .andExpect(jsonPath("$.id").exists())
                .andReturn();

        // The rest of the test (READ, UPDATE, DELETE) remains the same...
        String responseBody = createResult.getResponse().getContentAsString();
        Integer createdProfileId = objectMapper.readTree(responseBody).get("id").asInt();

        // 2. READ
        mockMvc.perform(get("/api/clients/" + createdProfileId)
                .header("Authorization", "Bearer " + brokerToken))
                .andExpect(status().isOk());

        // 3. UPDATE
        UpdateProfileRequest updateRequest = new UpdateProfileRequest();
        updateRequest.setOccupation("Senior Software Engineer");
        mockMvc.perform(put("/api/clients/" + createdProfileId)
                .header("Authorization", "Bearer " + brokerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());

        // 4. DELETE
        mockMvc.perform(delete("/api/clients/" + createdProfileId)
                .header("Authorization", "Bearer " + brokerToken))
                .andExpect(status().isNoContent());

        // 5. VERIFY DELETE
        mockMvc.perform(get("/api/clients/" + createdProfileId)
                .header("Authorization", "Bearer " + brokerToken))
                .andExpect(status().isNotFound());
    }
}