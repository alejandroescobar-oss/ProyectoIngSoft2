package com.example.questionbank.domain.model;

/** Optional editorial metadata for a question. */
public record QuestionMetadata(String cognitiveLevel, Integer estimatedMinutes, String editorialNotes) {
    /** Validates metadata values when they are supplied. */
    public QuestionMetadata {
        if (estimatedMinutes != null && estimatedMinutes < 1) {
            throw new IllegalArgumentException("Estimated minutes must be positive");
        }
    }
}
