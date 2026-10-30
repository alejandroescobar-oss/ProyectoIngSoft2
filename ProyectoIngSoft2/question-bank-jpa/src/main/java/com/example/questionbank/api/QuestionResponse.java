package com.example.questionbank.api;

import com.example.questionbank.domain.model.*;
import java.util.List;
import java.util.Set;

/** Stable API representation of either supported question subtype. */
public record QuestionResponse(Long id, String type, String statement, Difficulty difficulty,
        QuestionStatus status, Long authorId, Long topicId, Set<Long> competencyIds,
        QuestionMetadata metadata, List<AnswerOption> options, Boolean expectedAnswer) {
    /** Builds an API response without exposing JPA records. */
    public static QuestionResponse from(Question question) {
        if (question instanceof MultipleChoiceQuestion multipleChoice) {
            return new QuestionResponse(question.getId(), "MULTIPLE_CHOICE", question.getStatement(),
                    question.getDifficulty(), question.getStatus(), question.getAuthorId(), question.getTopicId(),
                    question.getCompetencyIds(), question.getMetadata(), multipleChoice.getOptions(), null);
        }
        TrueFalseQuestion trueFalse = (TrueFalseQuestion) question;
        return new QuestionResponse(question.getId(), "TRUE_FALSE", question.getStatement(),
                question.getDifficulty(), question.getStatus(), question.getAuthorId(), question.getTopicId(),
                question.getCompetencyIds(), question.getMetadata(), List.of(), trueFalse.isExpectedAnswer());
    }
}
