package com.danielbohry.authservice.api;

import com.danielbohry.authservice.service.auth.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("api")
public class AuthController {

    private final AuthService service;

    @PostMapping("register")
    public ResponseEntity<AuthenticationResponse> signup(@RequestBody AuthenticationRequest request) {
        log.info("New signup for username [{}]", request.getUsername());
        var response = service.signup(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("authenticate")
    public ResponseEntity<AuthenticationResponse> signin(@RequestBody AuthenticationRequest request) {
        log.info("New signin for username [{}]", request.getUsername());
        var response = service.signin(request);
        return ResponseEntity.ok(response);
    }

}
