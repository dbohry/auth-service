package com.danielbohry.authservice.service.auth;

import com.danielbohry.authservice.api.dto.AuthenticationRequest;
import com.danielbohry.authservice.api.dto.AuthenticationResponse;
import com.danielbohry.authservice.domain.ApplicationUser;
import com.danielbohry.authservice.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.danielbohry.authservice.service.auth.UserConverter.convert;

@Service
@AllArgsConstructor
public class AuthService implements UserDetailsService {

    private final UserService service;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = service.findByUsername(username);
        var authorities = user.getRoles().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).toList();

        return new User(user.getUsername(), user.getPassword(), authorities);
    }

    public AuthenticationResponse signup(AuthenticationRequest request) {
        UserDetails user = User.builder().username(request.getUsername()).password(passwordEncoder.encode(request.getPassword())).build();
        ApplicationUser saved = service.create(convert(user));
        Authentication authentication = jwtService.generateToken(saved);
        return buildResponse(saved.getId(), authentication);
    }

    public AuthenticationResponse signin(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
            request.getUsername(), request.getPassword())
        );
        ApplicationUser user = service.findByUsername(request.getUsername());
        Authentication authentication = jwtService.generateToken(user);
        return buildResponse(user.getId(), authentication);
    }

    private static AuthenticationResponse buildResponse(String id, Authentication authentication) {
        return AuthenticationResponse.builder()
            .id(id)
            .username(authentication.username())
            .token(authentication.token())
            .expirationDate(authentication.expirationDate())
            .roles(authentication.authorities())
            .build();
    }

}
