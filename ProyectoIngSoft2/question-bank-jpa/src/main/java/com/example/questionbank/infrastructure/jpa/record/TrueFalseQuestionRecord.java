package com.example.questionbank.infrastructure.jpa.record;

import com.example.questionbank.domain.model.Difficulty;
import com.example.questionbank.domain.model.QuestionStatus;
import jakarta.persistence.*;

/** JPA subtype containing the expected truth value. */
@Entity
@Table(name = "true_false_questions")
public class TrueFalseQuestionRecord extends QuestionRecord {
    @Column(name = "expected_answer", nullable = false)
    private boolean expectedAnswer;

    protected TrueFalseQuestionRecord() { }
    /** Creates a true/false question record. */
    public TrueFalseQuestionRecord(String statement, Difficulty difficulty, QuestionStatus status,
                                   UserRecord author, TopicRecord topic, boolean expectedAnswer) {
        super(statement, difficulty, status, author, topic);
        this.expectedAnswer = expectedAnswer;
    }
    /** @return expected truth value */
    public boolean isExpectedAnswer() { return expectedAnswer; }
}
