package com.example.questionbank.domain.model;

/** Academic topic used to classify questions. */
public record Topic(Long id, String name) {
    /** Validates the topic name. */
    public Topic {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Topic name is required");
    }
}
