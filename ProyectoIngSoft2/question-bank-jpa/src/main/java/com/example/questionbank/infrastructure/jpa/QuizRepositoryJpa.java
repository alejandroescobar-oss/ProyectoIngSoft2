package com.example.questionbank.infrastructure.jpa;

import com.example.questionbank.domain.model.Quiz;
import com.example.questionbank.domain.repository.QuizRepository;
import com.example.questionbank.infrastructure.jpa.record.QuestionRecord;
import com.example.questionbank.infrastructure.jpa.record.QuizRecord;
import com.example.questionbank.infrastructure.jpa.repository.SpringQuestionDataRepository;
import com.example.questionbank.infrastructure.jpa.repository.SpringQuizDataRepository;
import com.example.questionbank.infrastructure.jpa.repository.SpringUserDataRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/** JPA implementation of quiz persistence. */
@Repository
public class QuizRepositoryJpa implements QuizRepository {
    private final SpringQuizDataRepository quizzes;
    private final SpringUserDataRepository users;
    private final SpringQuestionDataRepository questions;

    /** Creates the adapter and its Spring Data dependencies. */
    public QuizRepositoryJpa(SpringQuizDataRepository quizzes, SpringUserDataRepository users,
                             SpringQuestionDataRepository questions) {
        this.quizzes = quizzes;
        this.users = users;
        this.questions = questions;
    }

    @Override
    @Transactional
    public Quiz save(Quiz quiz) {
        QuizRecord record = new QuizRecord(quiz.title(), users.getReferenceById(quiz.teacherId()));
        List<QuestionRecord> questionRecords = questions.findAllById(quiz.questionIds());
        record.getQuestions().addAll(questionRecords);
        QuizRecord saved = quizzes.save(record);
        return toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Quiz> findAll() { return quizzes.findAll().stream().map(QuizRepositoryJpa::toDomain).toList(); }

    private static Quiz toDomain(QuizRecord record) {
        return new Quiz(record.getId(), record.getTitle(), record.getTeacher().getId(),
                record.getQuestions().stream().map(QuestionRecord::getId).toList());
    }
}
