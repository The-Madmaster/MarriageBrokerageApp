package com.marriagebureau.usermanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marriagebureau.usermanagement.dto.LoginRequest;
import com.marriagebureau.usermanagement.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional // This ensures the database is rolled back to a clean state after each test
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc; // A powerful tool to simulate HTTP requests

    @Autowired
    private ObjectMapper objectMapper; // Helper to convert our Java objects to JSON

    @Test
    void testRegisterAndLoginFlow() throws Exception {
        // --- 1. Test the Registration Endpoint ---
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("testbroker@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setContactNumber("1234567890");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                // Assert that the request was successful (201 Created)
                .andExpect(status().isCreated())
                // Assert that the response body contains an accessToken and a tokenType
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.tokenType").value("Bearer"));

        // --- 2. Test the Login Endpoint with the same user ---
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("testbroker@example.com");
        loginRequest.setPassword("password123");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                // Assert that the login was successful (200 OK)
                .andExpect(status().isOk())
                // Assert that the response also contains a valid token
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }
}