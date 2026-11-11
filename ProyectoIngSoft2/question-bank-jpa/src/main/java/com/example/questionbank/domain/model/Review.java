package com.example.questionbank.domain.model;

import java.time.Instant;

/** Domain record of a review decision, independent of its JPA representation. */
public record Review(Long id, Long questionId, Long reviewerId, ReviewDecision decision,
                     String comments, Instant createdAt) {
    /** Validates identifiers, decision, and timestamp. */
    public Review {
        if (questionId == null || reviewerId == null || decision == null || createdAt == null) {
            throw new IllegalArgumentException("Question, reviewer, decision, and timestamp are required");
        }
    }
}
