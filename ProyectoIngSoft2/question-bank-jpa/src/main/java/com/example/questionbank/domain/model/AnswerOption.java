package com.example.questionbank.domain.model;

/** An answer candidate belonging to a multiple-choice question. */
public record AnswerOption(Long id, String text, boolean correct) {
    /** Validates the option text. */
    public AnswerOption {
        if (text == null || text.isBlank()) throw new IllegalArgumentException("Option text is required");
    }
}
