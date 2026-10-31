package com.example.questionbank.application;

import com.example.questionbank.domain.model.*;
import com.example.questionbank.domain.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.List;

/** Coordinates quiz creation and enforces publication rules. */
@Service
@Transactional
public class QuizService {
    private final QuizRepository quizzes;
    private final QuestionRepository questions;
    private final UserRepository users;

    /** Injects technology-independent persistence contracts. */
    public QuizService(QuizRepository quizzes, QuestionRepository questions, UserRepository users) {
        this.quizzes = quizzes;
        this.questions = questions;
        this.users = users;
    }

    /** Creates a quiz only from unique, published questions and a teacher account. */
    public Quiz create(String title, Long teacherId, List<Long> questionIds) {
        User teacher = users.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("User " + teacherId + " was not found"));
        if (teacher.role() != Role.TEACHER) throw new IllegalArgumentException("Quiz owner must have role TEACHER");
        if (questionIds == null || questionIds.isEmpty()) throw new IllegalArgumentException("A quiz needs at least one question");
        if (new HashSet<>(questionIds).size() != questionIds.size()) {
            throw new IllegalArgumentException("A quiz cannot contain duplicate questions");
        }
        for (Long questionId : questionIds) {
            Question question = questions.findById(questionId)
                    .orElseThrow(() -> new ResourceNotFoundException("Question " + questionId + " was not found"));
            if (question.getStatus() != QuestionStatus.PUBLISHED) {
                throw new IllegalStateException("Only published questions can be added to a quiz");
            }
        }
        return quizzes.save(new Quiz(null, title, teacherId, questionIds));
    }

    /** Lists saved quizzes. */
    @Transactional(readOnly = true)
    public List<Quiz> list() { return quizzes.findAll(); }
}
