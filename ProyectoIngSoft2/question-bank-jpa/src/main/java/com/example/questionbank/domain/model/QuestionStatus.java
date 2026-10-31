package com.example.questionbank.domain.model;

/** States allowed by the question review and publication lifecycle. */
public enum QuestionStatus {
    DRAFT, PENDING_REVIEW, IN_REVIEW, APPROVED, PUBLISHED, ARCHIVED
}
