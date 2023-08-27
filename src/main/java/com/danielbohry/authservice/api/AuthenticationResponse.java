package com.danielbohry.authservice.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationResponse {

    private final String token;

}
