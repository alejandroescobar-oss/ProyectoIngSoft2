package com.example.questionbank.api.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

/** Request to update the classification of an existing draft question. */
public record QuestionClassificationRequest(@NotNull Long authorId, @NotNull Long topicId,
                                            @NotEmpty Set<@NotNull Long> competencyIds) { }
