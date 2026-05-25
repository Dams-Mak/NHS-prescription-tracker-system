package com.gpappointments.auth.service;

import com.gpappointments.auth.model.Credential;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {

    private final AuthRepository authRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(AuthRepository authRepository,
                       JwtService jwtService) {
        this.authRepository = authRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    // patient signup
    public Map<String, String> patientSignup(
            String email, String password,
            String forename, String surname) {

        if (authRepository.emailExists(email)) {
            throw new RuntimeException(
                    "An account with this email already exists.");
        }

        Credential credential = new Credential();
        credential.setEmail(email);
        credential.setPasswordHash(
                passwordEncoder.encode(password));
        credential.setRole("PATIENT");
        credential.setForename(forename);
        credential.setSurname(surname);

        Credential saved = authRepository.save(credential);
        String token = jwtService.generateToken(
                saved.getId(), "PATIENT");

        return Map.of(
                "token", token,
                "userId", saved.getId(),
                "role", "PATIENT",
                "forename", forename,
                "message", "Patient account created successfully."
        );
    }

    // GP signup
    public Map<String, String> gpSignup(
            String email, String password,
            String forename, String surname,
            String practiceCode) {

        if (authRepository.emailExists(email)) {
            throw new RuntimeException(
                    "An account with this email already exists.");
        }

        Credential credential = new Credential();
        credential.setEmail(email);
        credential.setPasswordHash(
                passwordEncoder.encode(password));
        credential.setRole("GP");
        credential.setForename(forename);
        credential.setSurname(surname);
        credential.setPracticeCode(practiceCode);

        Credential saved = authRepository.save(credential);
        String token = jwtService.generateToken(
                saved.getId(), "GP");

        return Map.of(
                "token", token,
                "userId", saved.getId(),
                "role", "GP",
                "forename", forename,
                "message", "GP account created successfully."
        );
    }

    // clerk signup
    public Map<String, String> clerkSignup(
            String email, String password,
            String forename, String surname) {

        if (authRepository.emailExists(email)) {
            throw new RuntimeException(
                    "An account with this email already exists.");
        }

        Credential credential = new Credential();
        credential.setEmail(email);
        credential.setPasswordHash(
                passwordEncoder.encode(password));
        credential.setRole("CLERK");
        credential.setForename(forename);
        credential.setSurname(surname);

        Credential saved = authRepository.save(credential);
        String token = jwtService.generateToken(
                saved.getId(), "CLERK");

        return Map.of(
                "token", token,
                "userId", saved.getId(),
                "role", "CLERK",
                "forename", forename,
                "message", "Clerk account created successfully."
        );
    }

    // pharmacist signup
    public Map<String, String> pharmacistSignup(
            String email, String password,
            String forename, String surname,
            String pharmacyName) {

        if (authRepository.emailExists(email)) {
            throw new RuntimeException(
                    "An account with this email already exists.");
        }

        Credential credential = new Credential();
        credential.setEmail(email);
        credential.setPasswordHash(
                passwordEncoder.encode(password));
        credential.setRole("PHARMACIST");
        credential.setForename(forename);
        credential.setSurname(surname);
        credential.setPharmacyName(pharmacyName);

        Credential saved = authRepository.save(credential);
        String token = jwtService.generateToken(
                saved.getId(), "PHARMACIST");

        return Map.of(
                "token", token,
                "userId", saved.getId(),
                "role", "PHARMACIST",
                "forename", forename,
                "message", "Pharmacist account created successfully."
        );
    }

    // login for all roles
    public Map<String, String> login(
            String email, String password) {

        Credential credential = authRepository
                .findByEmail(email)
                .orElseThrow(() -> new RuntimeException(
                        "No account found with this email."));

        boolean passwordMatches = passwordEncoder.matches(
                password, credential.getPasswordHash());

        if (!passwordMatches) {
            throw new RuntimeException(
                    "Incorrect password. Please try again.");
        }

        String token = jwtService.generateToken(
                credential.getId(),
                credential.getRole());

        return Map.of(
                "token", token,
                "userId", credential.getId(),
                "role", credential.getRole(),
                "forename", credential.getForename(),
                "message", "Login successful."
        );
    }
}
