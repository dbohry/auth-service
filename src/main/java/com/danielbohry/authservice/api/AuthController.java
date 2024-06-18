package com.danielbohry.authservice.api;

import com.danielbohry.authservice.api.dto.AuthenticationRequest;
import com.danielbohry.authservice.api.dto.AuthenticationResponse;
import com.danielbohry.authservice.domain.ApplicationUser;
import com.danielbohry.authservice.service.auth.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
        log.info("New signing for username [{}]", request.getUsername());
        var response = service.signin(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("authorize")
    public ResponseEntity<Object> authorize(@RequestParam(defaultValue = "USER", required = false) String authority) {
        SecurityContext context = SecurityContextHolder.getContext();
        Object principal = context.getAuthentication().getPrincipal();
        if (principal instanceof ApplicationUser user) {
            if (user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList().contains(authority)) {
                return ResponseEntity.ok().build();
            }
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

}
