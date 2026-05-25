package com.gpappointments.auth.service;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;
import com.gpappointments.auth.model.Credential;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class AuthRepository {

    private static final RethinkDB r = RethinkDB.r;
    private static final String TABLE = "credentials";

    private final Connection conn;
    private final String database;

    public AuthRepository(
            Connection conn,
            @org.springframework.beans.factory.annotation
                    .Value("${rethinkdb.database}") String database) {
        this.conn = conn;
        this.database = database;
    }

    // saves a new credential record to RethinkDB
    public Credential save(Credential credential) {
        Map<String, Object> doc = new HashMap<>();
        doc.put("email", credential.getEmail());
        doc.put("passwordHash", credential.getPasswordHash());
        doc.put("role", credential.getRole());
        doc.put("forename", credential.getForename());
        doc.put("surname", credential.getSurname());
        doc.put("practiceCode", credential.getPracticeCode());
        doc.put("pharmacyName", credential.getPharmacyName());
        doc.put("createdAt", System.currentTimeMillis());

        Map result = r.db(database)
                .table(TABLE)
                .insert(doc)
                .run(conn, Map.class)
                .single();

        // RethinkDB returns the generated id
        String generatedId = ((java.util.List<String>)
                result.get("generated_keys")).get(0);
        credential.setId(generatedId);
        return credential;
    }

    // finds a user by email using the index we created
    public Optional<Credential> findByEmail(String email) {
        var result = r.db(database)
                .table(TABLE)
                .getAll(email)
                .optArg("index", "email")
                .run(conn, Map.class)
                .toList();

        if (result.isEmpty()) {
            return Optional.empty();
        }

        Map doc = result.get(0);
        Credential credential = new Credential();
        credential.setId((String) doc.get("id"));
        credential.setEmail((String) doc.get("email"));
        credential.setPasswordHash((String) doc.get("passwordHash"));
        credential.setRole((String) doc.get("role"));
        credential.setForename((String) doc.get("forename"));
        credential.setSurname((String) doc.get("surname"));
        credential.setPracticeCode((String) doc.get("practiceCode"));
        credential.setPharmacyName((String) doc.get("pharmacyName"));
        credential.setCreatedAt((Long) doc.get("createdAt"));
        return Optional.of(credential);
    }

    // checks if an email is already registered
    public boolean emailExists(String email) {
        return findByEmail(email).isPresent();
    }
}
