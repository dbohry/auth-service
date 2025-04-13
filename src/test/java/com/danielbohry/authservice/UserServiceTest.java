package com.danielbohry.authservice;

import com.danielbohry.authservice.domain.ApplicationUser;
import com.danielbohry.authservice.exceptions.BadRequestException;
import com.danielbohry.authservice.exceptions.NotFoundException;
import com.danielbohry.authservice.repository.UserRepository;
import com.danielbohry.authservice.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.danielbohry.authservice.domain.Role.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    private UserService service;
    private UserRepository repository;

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String MAIL = "email@email.com";

    @BeforeEach
    public void setup() {
        repository = mock(UserRepository.class);
        service = new UserService(repository);
    }

    @Test
    public void shouldCreateApplicationUser() {
        //given
        ApplicationUser user = someUser();

        when(repository.save(user)).thenReturn(user);

        //when
        ApplicationUser result = service.create(user);

        //then
        assertEquals(user, result);
    }

    @Test
    public void shouldFindUserByUsername() {
        //given
        ApplicationUser user = someUser();

        when(repository.findByUsernameAndActiveTrue(USERNAME)).thenReturn(Optional.of(user));

        //when
        ApplicationUser result = service.findByUsername(USERNAME);

        //then
        assertEquals(user, result);
    }

    @Test
    public void shouldThrowNotFoundExceptionWhenUserNotFound() {
        // given
        String unknownUsername = "unknown_user";
        when(repository.findByUsernameAndActiveTrue(unknownUsername)).thenReturn(Optional.empty());

        // then
        assertThrows(NotFoundException.class, () -> service.findByUsername(unknownUsername));
    }

    @Test
    public void shouldValidateUsername() {
        //given
        ApplicationUser user = someUser();

        when(repository.existsByUsername(USERNAME)).thenReturn(true);

        //then
        assertThrows(BadRequestException.class, () -> service.create(user));
    }

    private static ApplicationUser someUser() {
        return ApplicationUser.builder()
            .id(UUID.randomUUID().toString())
            .username(USERNAME)
            .password(PASSWORD)
            .email(MAIL)
            .roles(List.of(USER))
            .active(true)
            .build();
    }

}
