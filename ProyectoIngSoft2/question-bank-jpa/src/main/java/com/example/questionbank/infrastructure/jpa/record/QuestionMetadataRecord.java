package com.example.questionbank.infrastructure.jpa.record;

import jakarta.persistence.*;

/** Optional one-to-one JPA record for editorial question metadata. */
@Entity
@Table(name = "question_metadata")
public class QuestionMetadataRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "cognitive_level")
    private String cognitiveLevel;
    @Column(name = "estimated_minutes")
    private Integer estimatedMinutes;
    @Column(name = "editorial_notes", length = 2000)
    private String editorialNotes;
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false, unique = true)
    private QuestionRecord question;

    protected QuestionMetadataRecord() { }
    /** Creates a metadata record. */
    public QuestionMetadataRecord(String cognitiveLevel, Integer estimatedMinutes, String editorialNotes) {
        this.cognitiveLevel = cognitiveLevel;
        this.estimatedMinutes = estimatedMinutes;
        this.editorialNotes = editorialNotes;
    }
    /** @return cognitive level */
    public String getCognitiveLevel() { return cognitiveLevel; }
    /** Updates the cognitive level. */
    public void setCognitiveLevel(String cognitiveLevel) { this.cognitiveLevel = cognitiveLevel; }
    /** @return estimated response time */
    public Integer getEstimatedMinutes() { return estimatedMinutes; }
    /** Updates the estimated response time. */
    public void setEstimatedMinutes(Integer estimatedMinutes) { this.estimatedMinutes = estimatedMinutes; }
    /** @return editorial notes */
    public String getEditorialNotes() { return editorialNotes; }
    /** Updates the editorial notes. */
    public void setEditorialNotes(String editorialNotes) { this.editorialNotes = editorialNotes; }
    /** Sets the question owning this metadata. */
    public void setQuestion(QuestionRecord question) { this.question = question; }
}
