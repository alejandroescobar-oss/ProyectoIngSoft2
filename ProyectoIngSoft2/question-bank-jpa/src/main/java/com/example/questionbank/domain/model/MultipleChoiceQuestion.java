package com.example.questionbank.domain.model;

import java.util.List;
import java.util.Set;

/** Question with five answer choices and exactly one correct answer when submitted. */
public final class MultipleChoiceQuestion extends Question {
    private final List<AnswerOption> options;

    /** Creates a multiple-choice question. */
    public MultipleChoiceQuestion(Long id, String statement, Difficulty difficulty, QuestionStatus status,
                                  Long authorId, Long topicId, Set<Long> competencyIds,
                                  QuestionMetadata metadata, List<AnswerOption> options) {
        super(id, statement, difficulty, status, authorId, topicId, competencyIds, metadata);
        this.options = options == null ? List.of() : List.copyOf(options);
    }

    /** @return immutable answer choices */
    public List<AnswerOption> getOptions() { return options; }
}
