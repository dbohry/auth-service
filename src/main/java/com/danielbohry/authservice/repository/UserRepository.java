package com.danielbohry.authservice.repository;

import com.danielbohry.authservice.domain.ApplicationUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<ApplicationUser, String> {

    boolean existsByUsername(String username);

    Optional<ApplicationUser> findByUsernameAndActiveTrue(String username);

}
