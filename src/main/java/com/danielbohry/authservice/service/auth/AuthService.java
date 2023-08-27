package com.danielbohry.authservice.service.auth;

import com.danielbohry.authservice.api.AuthenticationRequest;
import com.danielbohry.authservice.api.AuthenticationResponse;
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
import static java.util.Collections.singletonList;

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
        var authority = new SimpleGrantedAuthority("ROLE_" + user.getRole());

        return new User(user.getUsername(), user.getPassword(), singletonList(authority));
    }

    public AuthenticationResponse signup(AuthenticationRequest request) {
        var user = User.builder().username(request.getUsername()).password(passwordEncoder.encode(request.getPassword())).build();
        service.save(convert(user));
        var jwt = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwt).build();
    }

    public AuthenticationResponse signin(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(), request.getPassword())
        );
        var user = service.findByUsername(request.getUsername());
        var jwt = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwt).build();
    }

}
