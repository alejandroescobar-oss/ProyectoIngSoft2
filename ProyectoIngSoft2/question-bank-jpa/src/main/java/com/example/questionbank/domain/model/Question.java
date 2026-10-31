package com.example.questionbank.domain.model;

import java.util.Set;

/** Persistence-independent base model for all question types. */
public abstract class Question {
    private final Long id;
    private String statement;
    private Difficulty difficulty;
    private QuestionStatus status;
    private final Long authorId;
    private Long topicId;
    private Set<Long> competencyIds;
    private QuestionMetadata metadata;

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

    /** Edits the statement, difficulty and metadata of a draft question by its author. */
    public void edit(Long editorId, String statement, Difficulty difficulty, QuestionMetadata metadata) {
        verifyDraftAndAuthor(editorId);
        if (statement == null || statement.isBlank()) throw new IllegalArgumentException("Statement is required");
        if (difficulty == null) throw new IllegalArgumentException("Difficulty is required");
        this.statement = statement;
        this.difficulty = difficulty;
        this.metadata = metadata;
    }

    /** Reclassifies the topic and competencies of a draft question by its author. */
    public void reclassify(Long editorId, Long newTopicId, Set<Long> newCompetencyIds) {
        verifyDraftAndAuthor(editorId);
        if (newTopicId == null) throw new IllegalArgumentException("Topic is required");
        if (newCompetencyIds == null || newCompetencyIds.isEmpty()) {
            throw new IllegalArgumentException("At least one competency is required");
        }
        this.topicId = newTopicId;
        this.competencyIds = Set.copyOf(newCompetencyIds);
    }

    /** Validates that the question is in DRAFT state and the user is the original author. */
    protected void verifyDraftAndAuthor(Long editorId) {
        if (status != QuestionStatus.DRAFT) {
            throw new IllegalStateException("Only questions in DRAFT status can be modified");
        }
        if (!authorId.equals(editorId)) {
            throw new IllegalArgumentException("Only the author can modify this question");
        }
    }

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
