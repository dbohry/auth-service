package com.danielbohry.authservice.service.auth;

import java.time.Instant;
import java.util.List;

public record Authentication(String token, Instant expirationDate, String username, List<String> authorities) {
}
