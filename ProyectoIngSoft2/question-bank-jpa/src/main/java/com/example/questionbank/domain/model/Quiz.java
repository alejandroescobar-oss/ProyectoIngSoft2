package com.example.questionbank.domain.model;

import java.util.List;

/** A teacher-authored quiz composed exclusively of published questions. */
public record Quiz(Long id, String title, Long teacherId, List<Long> questionIds) {
    /** Validates required quiz data and defensively copies question identifiers. */
    public Quiz {
        if (title == null || title.isBlank()) throw new IllegalArgumentException("Quiz title is required");
        if (teacherId == null) throw new IllegalArgumentException("Teacher is required");
        questionIds = questionIds == null ? List.of() : List.copyOf(questionIds);
        if (questionIds.isEmpty()) throw new IllegalArgumentException("A quiz needs at least one question");
    }
}
