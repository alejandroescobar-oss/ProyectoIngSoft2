package com.example.questionbank.api.requests;

import com.example.questionbank.domain.model.AnswerOption;
import jakarta.validation.constraints.NotBlank;

/** HTTP payload for one multiple-choice answer option. */
public record AnswerOptionRequest(@NotBlank String text, boolean correct) {
    /** Converts this HTTP payload into the domain model. */
    public AnswerOption toDomain() { return new AnswerOption(null, text, correct); }
}
