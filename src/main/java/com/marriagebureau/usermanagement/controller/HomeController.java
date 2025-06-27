package com.marriagebureau.usermanagement.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController; // Or @Controller

@RestController // This annotation tells Spring Boot to treat this as a REST controller
public class HomeController {

    @GetMapping("/") // This maps HTTP GET requests to the root URL "/"
    public String home() {
        return "Welcome to the Marriage Bureau Software!"; // Returns a simple string
    }

    @GetMapping("/about") // You can add more endpoints
    public String about() {
        return "This is a software for managing marriage profiles.";
    }
}