package com.example.questionbank.infrastructure.jpa.record;

import com.example.questionbank.domain.model.Difficulty;
import com.example.questionbank.domain.model.QuestionStatus;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/** JPA subtype containing the options of a multiple-choice question. */
@Entity
@Table(name = "multiple_choice_questions")
public class MultipleChoiceQuestionRecord extends QuestionRecord {
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    private List<AnswerOptionRecord> options = new ArrayList<>();

    protected MultipleChoiceQuestionRecord() { }
    /** Creates a multiple-choice question record. */
    public MultipleChoiceQuestionRecord(String statement, Difficulty difficulty, QuestionStatus status,
                                        UserRecord author, TopicRecord topic) {
        super(statement, difficulty, status, author, topic);
    }
    /** @return ordered answer options */
    public List<AnswerOptionRecord> getOptions() { return options; }
    /** Adds an option and sets its owning question. */
    public void addOption(AnswerOptionRecord option) {
        options.add(option);
        option.setQuestion(this);
    }
}
