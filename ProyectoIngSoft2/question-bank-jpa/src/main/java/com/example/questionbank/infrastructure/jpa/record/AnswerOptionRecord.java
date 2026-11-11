package com.example.questionbank.infrastructure.jpa.record;

import jakarta.persistence.*;

/** JPA representation of an answer option, owned by a multiple-choice question. */
@Entity
@Table(name = "answer_options")
public class AnswerOptionRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 2000)
    private String text;
    @Column(nullable = false)
    private boolean correct;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    private MultipleChoiceQuestionRecord question;

    protected AnswerOptionRecord() { }
    /** Creates an answer-option record. */
    public AnswerOptionRecord(String text, boolean correct) {
        this.text = text;
        this.correct = correct;
    }
    /** @return database identifier */
    public Long getId() { return id; }
    /** @return option text */
    public String getText() { return text; }
    /** @return whether the answer is correct */
    public boolean isCorrect() { return correct; }
    /** Sets the owning question. */
    public void setQuestion(MultipleChoiceQuestionRecord question) { this.question = question; }
}
