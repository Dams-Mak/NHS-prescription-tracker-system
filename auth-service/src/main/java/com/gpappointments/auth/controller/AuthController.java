package com.gpappointments.auth.controller;

import com.gpappointments.auth.service.AuthService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @MutationMapping
    public Map<String, String> patientSignup(
            @Argument String email,
            @Argument String password,
            @Argument String forename,
            @Argument String surname) {

        return authService.patientSignup(
                email, password, forename, surname);
    }

    @MutationMapping
    public Map<String, String> gpSignup(
            @Argument String email,
            @Argument String password,
            @Argument String forename,
            @Argument String surname,
            @Argument String practiceCode) {

        return authService.gpSignup(
                email, password, forename, surname, practiceCode);
    }

    @MutationMapping
    public Map<String, String> clerkSignup(
            @Argument String email,
            @Argument String password,
            @Argument String forename,
            @Argument String surname) {

        return authService.clerkSignup(
                email, password, forename, surname);
    }

    @MutationMapping
    public Map<String, String> pharmacistSignup(
            @Argument String email,
            @Argument String password,
            @Argument String forename,
            @Argument String surname,
            @Argument String pharmacyName) {

        return authService.pharmacistSignup(
                email, password, forename, surname, pharmacyName);
    }

    @MutationMapping
    public Map<String, String> login(
            @Argument String email,
            @Argument String password) {

        return authService.login(email, password);
    }
}
