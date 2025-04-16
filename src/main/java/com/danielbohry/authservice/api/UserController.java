package com.danielbohry.authservice.api;

import com.danielbohry.authservice.api.dto.UserResponse;
import com.danielbohry.authservice.domain.ApplicationUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("api/users")
public class UserController {

    @GetMapping("current")
    public ResponseEntity<?> get() {
        SecurityContext context = SecurityContextHolder.getContext();
        Object principal = context.getAuthentication().getPrincipal();
        if (principal instanceof ApplicationUser user) {
            return ResponseEntity.ok(new UserResponse(user.getId(), user.getUsername(), user.getRoles().stream().map(Enum::toString).toList()));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<Object>> getAll() {
        return ResponseEntity.ok(new ArrayList<>());
    }

}
