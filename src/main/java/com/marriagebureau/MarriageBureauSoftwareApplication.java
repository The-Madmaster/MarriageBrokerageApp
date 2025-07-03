// src/main/java/com/marriagebureau/MarriageBureauSoftwareApplication.java
package com.marriagebureau;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication // Marks this as a Spring Boot application. Implicitly includes @ComponentScan.
@EnableJpaAuditing // Enables JPA Auditing to automatically populate @CreatedDate and @LastModifiedDate.
public class MarriageBureauSoftwareApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarriageBureauSoftwareApplication.class, args);
    }

}
