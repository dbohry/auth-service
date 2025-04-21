package com.danielbohry.authservice.api.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class AuthenticationResponse {

    private final String id;
    private final String username;
    private final String token;
    private final Instant expirationDate;
    private final List<String> roles;

}
