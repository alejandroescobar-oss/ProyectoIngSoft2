package com.example.questionbank.domain.repository;

import com.example.questionbank.domain.model.Quiz;
import java.util.List;

/** Technology-independent persistence contract for quizzes. */
public interface QuizRepository {
    /** Saves a quiz and returns it with its assigned identifier. */
    Quiz save(Quiz quiz);
    /** Lists saved quizzes. */
    List<Quiz> findAll();
}
