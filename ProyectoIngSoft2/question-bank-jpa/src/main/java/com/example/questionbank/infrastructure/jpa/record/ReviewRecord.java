package com.example.questionbank.infrastructure.jpa.record;

import com.example.questionbank.domain.model.ReviewDecision;
import jakarta.persistence.*;
import java.time.Instant;

/** JPA record of a reviewer decision and its comments. */
@Entity
@Table(name = "question_reviews")
public class ReviewRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    private QuestionRecord question;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reviewer_id", nullable = false)
    private UserRecord reviewer;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private ReviewDecision decision;
    @Column(length = 2000)
    private String comments;
    @Column(nullable = false)
    private Instant createdAt;

    protected ReviewRecord() { }
    /** Creates a review record. */
    public ReviewRecord(QuestionRecord question, UserRecord reviewer, ReviewDecision decision, String comments,
                        Instant createdAt) {
        this.question = question;
        this.reviewer = reviewer;
        this.decision = decision;
        this.comments = comments;
        this.createdAt = createdAt;
    }
    /** @return generated identifier */
    public Long getId() { return id; }
    /** @return reviewed question */
    public QuestionRecord getQuestion() { return question; }
    /** @return reviewer */
    public UserRecord getReviewer() { return reviewer; }
    /** @return review decision */
    public ReviewDecision getDecision() { return decision; }
    /** @return reviewer comments */
    public String getComments() { return comments; }
    /** @return decision timestamp */
    public Instant getCreatedAt() { return createdAt; }
}
