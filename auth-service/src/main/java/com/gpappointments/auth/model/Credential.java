package com.gpappointments.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Credential {

    private String id;
    private String email;
    private String passwordHash;
    private String role;
    private String forename;
    private String surname;
    private String practiceCode;   
    private String pharmacyName;
    private Long createdAt;
}
