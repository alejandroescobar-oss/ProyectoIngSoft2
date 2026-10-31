package com.example.questionbank.domain.model;

/** Competency evaluated by one or more questions. */
public record Competency(Long id, String name) {
    /** Validates the competency name. */
    public Competency {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Competency name is required");
    }
}
