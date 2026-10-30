package com.example.questionbank.infrastructure.jpa.record;

import jakarta.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

/** JPA representation of a teacher's quiz. */
@Entity
@Table(name = "quizzes")
public class QuizRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "teacher_id", nullable = false)
    private UserRecord teacher;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "quiz_questions",
            joinColumns = @JoinColumn(name = "quiz_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id"))
    private Set<QuestionRecord> questions = new LinkedHashSet<>();

    protected QuizRecord() { }
    /** Creates a quiz record. */
    public QuizRecord(String title, UserRecord teacher) {
        this.title = title;
        this.teacher = teacher;
    }
    /** @return database identifier */
    public Long getId() { return id; }
    /** @return quiz title */
    public String getTitle() { return title; }
    /** @return teacher record */
    public UserRecord getTeacher() { return teacher; }
    /** @return included question records */
    public Set<QuestionRecord> getQuestions() { return questions; }
}
