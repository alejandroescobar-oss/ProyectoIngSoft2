package com.example.questionbank.domain.repository;

import com.example.questionbank.domain.model.Question;
import com.example.questionbank.domain.model.QuestionStatus;
import java.util.List;
import java.util.Optional;

/** Technology-independent persistence contract for questions. */
public interface QuestionRepository {
    /** Saves a question and returns it with its assigned identifier. */
    Question save(Question question);
    /** Finds a question by identifier. */
    Optional<Question> findById(Long id);
    /** Lists all question types. */
    List<Question> findAll();
    /** Lists questions authored by a user. */
    List<Question> findByAuthorId(Long authorId);
    /** Lists questions in a lifecycle state. */
    List<Question> findByStatus(QuestionStatus status);
    /** Finds published questions linked to a competency. */
    List<Question> findPublishedByCompetency(Long competencyId);
}
