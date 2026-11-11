package com.example.questionbank.infrastructure.jpa.record;

import com.example.questionbank.domain.model.Difficulty;
import com.example.questionbank.domain.model.QuestionStatus;
import jakarta.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

/** Shared JPA mapping for question types using the JOINED inheritance strategy. */
@Entity
@Table(name = "questions")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class QuestionRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 4000)
    private String statement;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private Difficulty difficulty;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private QuestionStatus status;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private UserRecord author;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "topic_id", nullable = false)
    private TopicRecord topic;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "question_competencies",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "competency_id"))
    private Set<CompetencyRecord> competencies = new LinkedHashSet<>();
    @OneToOne(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private QuestionMetadataRecord metadata;

    protected QuestionRecord() { }
    /** Sets fields shared by all question subtypes. */
    protected QuestionRecord(String statement, Difficulty difficulty, QuestionStatus status,
                             UserRecord author, TopicRecord topic) {
        this.statement = statement;
        this.difficulty = difficulty;
        this.status = status;
        this.author = author;
        this.topic = topic;
    }
    /** @return database identifier */
    public Long getId() { return id; }
    /** @return question statement */
    public String getStatement() { return statement; }
    /** @return question difficulty */
    public Difficulty getDifficulty() { return difficulty; }
    /** @return lifecycle state */
    public QuestionStatus getStatus() { return status; }
    /** Sets the lifecycle state after a domain transition. */
    public void setStatus(QuestionStatus status) { this.status = status; }
    /** @return author record */
    public UserRecord getAuthor() { return author; }
    /** @return topic record */
    public TopicRecord getTopic() { return topic; }
    /** @return competency records */
    public Set<CompetencyRecord> getCompetencies() { return competencies; }
    /** @return optional metadata record */
    public QuestionMetadataRecord getMetadata() { return metadata; }
    /** Sets the metadata and its owning relationship. */
    public void setMetadata(QuestionMetadataRecord metadata) {
        this.metadata = metadata;
        if (metadata != null) metadata.setQuestion(this);
    }
}
