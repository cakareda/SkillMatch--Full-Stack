package com.skillmatch.backend.repository;

import com.skillmatch.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // used to find a user by email address
    Optional<User> findByEmail(String email);
}
