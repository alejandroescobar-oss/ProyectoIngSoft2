package com.example.questionbank.domain.model;

import java.util.Set;

/** Question whose expected answer is true or false. */
public final class TrueFalseQuestion extends Question {
    private final boolean expectedAnswer;

    /** Creates a true/false question. */
    public TrueFalseQuestion(Long id, String statement, Difficulty difficulty, QuestionStatus status,
                             Long authorId, Long topicId, Set<Long> competencyIds,
                             QuestionMetadata metadata, boolean expectedAnswer) {
        super(id, statement, difficulty, status, authorId, topicId, competencyIds, metadata);
        this.expectedAnswer = expectedAnswer;
    }

    /** @return expected truth value */
    public boolean isExpectedAnswer() { return expectedAnswer; }
}
