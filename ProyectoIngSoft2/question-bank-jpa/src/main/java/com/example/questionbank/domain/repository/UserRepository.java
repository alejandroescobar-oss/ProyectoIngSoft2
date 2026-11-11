package com.example.questionbank.domain.repository;

import com.example.questionbank.domain.model.User;
import java.util.List;
import java.util.Optional;

/** Technology-independent persistence contract for users. */
public interface UserRepository {
    /** Saves a user and returns it with its assigned identifier. */
    User save(User user);
    /** Finds a user by identifier. */
    Optional<User> findById(Long id);
    /** Lists all users. */
    List<User> findAll();
}
