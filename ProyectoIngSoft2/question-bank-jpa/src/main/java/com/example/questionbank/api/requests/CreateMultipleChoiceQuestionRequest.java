package com.example.questionbank.api.requests;

import com.example.questionbank.domain.model.Difficulty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/** HTTP payload for creating a multiple-choice question. */
public record CreateMultipleChoiceQuestionRequest(@NotBlank String statement, @NotNull Difficulty difficulty,
        @NotNull Long authorId, @NotNull Long topicId, @NotEmpty Set<Long> competencyIds,
        @Valid QuestionMetadataRequest metadata, @NotEmpty List<@Valid AnswerOptionRequest> options) { }
