package com.example.questionbank.infrastructure.jpa;

import com.example.questionbank.domain.model.User;
import com.example.questionbank.domain.repository.UserRepository;
import com.example.questionbank.infrastructure.jpa.record.UserRecord;
import com.example.questionbank.infrastructure.jpa.repository.SpringUserDataRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/** JPA implementation of the user repository contract. */
@Repository
public class UserRepositoryJpa implements UserRepository {
    private final SpringUserDataRepository data;
    /** Creates the adapter around the Spring Data repository. */
    public UserRepositoryJpa(SpringUserDataRepository data) { this.data = data; }
    @Override public User save(User user) {
        UserRecord saved = data.save(new UserRecord(user.fullName(), user.email(), user.role()));
        return toDomain(saved);
    }
    @Override public Optional<User> findById(Long id) { return data.findById(id).map(UserRepositoryJpa::toDomain); }
    @Override public List<User> findAll() { return data.findAll().stream().map(UserRepositoryJpa::toDomain).toList(); }
    private static User toDomain(UserRecord record) {
        return new User(record.getId(), record.getFullName(), record.getEmail(), record.getRole());
    }
}
