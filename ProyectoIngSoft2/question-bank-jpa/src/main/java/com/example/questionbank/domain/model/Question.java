package com.example.questionbank.domain.model;

import java.util.Set;

/** Persistence-independent base model for all question types. */
public abstract class Question {
    private final Long id;
    private final String statement;
    private final Difficulty difficulty;
    private QuestionStatus status;
    private final Long authorId;
    private final Long topicId;
    private final Set<Long> competencyIds;
    private final QuestionMetadata metadata;

    /** Creates a question with its business classification and current state. */
    protected Question(Long id, String statement, Difficulty difficulty, QuestionStatus status, Long authorId,
                       Long topicId, Set<Long> competencyIds, QuestionMetadata metadata) {
        if (statement == null || statement.isBlank()) throw new IllegalArgumentException("Statement is required");
        if (difficulty == null || status == null) throw new IllegalArgumentException("Difficulty and status are required");
        if (authorId == null || topicId == null) throw new IllegalArgumentException("Author and topic are required");
        this.id = id;
        this.statement = statement;
        this.difficulty = difficulty;
        this.status = status;
        this.authorId = authorId;
        this.topicId = topicId;
        this.competencyIds = competencyIds == null ? Set.of() : Set.copyOf(competencyIds);
        this.metadata = metadata;
    }

    /** @return persistent identifier, or null before saving */
    public Long getId() { return id; }
    /** @return question text */
    public String getStatement() { return statement; }
    /** @return difficulty */
    public Difficulty getDifficulty() { return difficulty; }
    /** @return lifecycle state */
    public QuestionStatus getStatus() { return status; }
    /** @return author identifier */
    public Long getAuthorId() { return authorId; }
    /** @return topic identifier */
    public Long getTopicId() { return topicId; }
    /** @return immutable competency identifiers */
    public Set<Long> getCompetencyIds() { return competencyIds; }
    /** @return optional editorial metadata */
    public QuestionMetadata getMetadata() { return metadata; }

    /** Moves a draft to the review queue after application validation. */
    public void submitForReview() { transition(QuestionStatus.DRAFT, QuestionStatus.PENDING_REVIEW); }
    /** Assigns a queued question to a reviewer. */
    public void startReview() { transition(QuestionStatus.PENDING_REVIEW, QuestionStatus.IN_REVIEW); }
    /** Approves a question currently under review. */
    public void approve() { transition(QuestionStatus.IN_REVIEW, QuestionStatus.APPROVED); }
    /** Returns a rejected question to draft for correction. */
    public void reject() { transition(QuestionStatus.IN_REVIEW, QuestionStatus.DRAFT); }
    /** Publishes an approved question. */
    public void publish() { transition(QuestionStatus.APPROVED, QuestionStatus.PUBLISHED); }
    /** Archives a published question. */
    public void archive() { transition(QuestionStatus.PUBLISHED, QuestionStatus.ARCHIVED); }

    private void transition(QuestionStatus expected, QuestionStatus next) {
        if (status != expected) throw new IllegalStateException("Cannot move question from " + status + " to " + next);
        status = next;
    }
}
