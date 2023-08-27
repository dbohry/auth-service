package com.danielbohry.authservice.service.auth;

import com.danielbohry.authservice.domain.ApplicationUser;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.userdetails.UserDetails;

@UtilityClass
public class UserConverter {

    public static ApplicationUser convert(UserDetails user) {
        return ApplicationUser.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }

}
