package com.danielbohry.authservice.service.user;

import com.danielbohry.authservice.domain.ApplicationUser;
import com.danielbohry.authservice.exceptions.BadRequestException;
import com.danielbohry.authservice.exceptions.NotFoundException;
import com.danielbohry.authservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.danielbohry.authservice.domain.Role.USER;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository repository;

    public UserDetailsService userDetailsService() {
        return username -> repository.findByUsernameAndActiveTrue(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public ApplicationUser findByUsername(String username) {
        return repository.findByUsernameAndActiveTrue(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public void save(ApplicationUser applicationUser) {
        validateUsername(applicationUser);

        applicationUser.setId(UUID.randomUUID().toString());
        applicationUser.setPassword(applicationUser.getPassword());
        applicationUser.setRole(USER);
        applicationUser.setActive(true);

        repository.save(applicationUser);
    }

    public ApplicationUser update(ApplicationUser applicationUser) {
        validateUsername(applicationUser);
        return repository.save(applicationUser);
    }

    private void validateUsername(ApplicationUser applicationUser) {
        boolean exists = repository.existsByUsername(applicationUser.getUsername());

        if (exists) {
            throw new BadRequestException("Username is already in use");
        }
    }

}
