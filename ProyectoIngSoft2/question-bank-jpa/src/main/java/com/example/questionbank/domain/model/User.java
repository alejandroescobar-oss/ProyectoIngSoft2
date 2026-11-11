package com.example.questionbank.domain.model;

import java.util.Objects;

/** A person who participates in the question-bank workflow. */
public record User(Long id, String fullName, String email, Role role) {
    /** Validates the business fields required for a user. */
    public User {
        if (fullName == null || fullName.isBlank()) throw new IllegalArgumentException("Full name is required");
        if (email == null || email.isBlank()) throw new IllegalArgumentException("Email is required");
        Objects.requireNonNull(role, "Role is required");
    }
}
