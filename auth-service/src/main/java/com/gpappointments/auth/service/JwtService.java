package com.gpappointments.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtService {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiry.hours}")
    private int expiryHours;

    // creates a signed JWT token containing userId and role
    public String generateToken(String userId, String role) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.builder()
                .subject(userId)
                .claim("role", role)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(
                        Instant.now().plus(expiryHours, ChronoUnit.HOURS)
                ))
                .signWith(key)
                .compact();
    }

    // checks the token is valid and not expired
    public Claims validateToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // pulls the userId out of a token
    public String extractUserId(String token) {
        return validateToken(token).getSubject();
    }

    // pulls the role out of a token
    public String extractRole(String token) {
        return validateToken(token).get("role", String.class);
    }
}