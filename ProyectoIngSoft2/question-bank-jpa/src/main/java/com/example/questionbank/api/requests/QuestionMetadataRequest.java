package com.example.questionbank.api.requests;

import com.example.questionbank.domain.model.QuestionMetadata;
import jakarta.validation.constraints.Positive;

/** Optional metadata included when creating a question. */
public record QuestionMetadataRequest(String cognitiveLevel, @Positive Integer estimatedMinutes,
                                      String editorialNotes) {
    /** Converts this HTTP payload into the domain value object. */
    public QuestionMetadata toDomain() {
        return new QuestionMetadata(cognitiveLevel, estimatedMinutes, editorialNotes);
    }
}
