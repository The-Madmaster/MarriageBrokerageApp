package com.marriagebureau;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles; // This import IS correct here

@SpringBootTest
@ActiveProfiles("test") // This annotation IS correct here
class MarriageBureauSoftwareApplicationTests {

    @Test
    void contextLoads() {
        // This test simply checks if the Spring application context loads successfully.
    }
}