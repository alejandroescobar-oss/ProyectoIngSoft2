package com.example.questionbank.infrastructure.jpa.repository;

import com.example.questionbank.domain.model.QuestionStatus;
import com.example.questionbank.infrastructure.jpa.record.QuestionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/** Spring Data implementation detail for question queries. */
public interface SpringQuestionDataRepository extends JpaRepository<QuestionRecord, Long> {
    /** Finds questions authored by the given user. */
    List<QuestionRecord> findAllByAuthor_Id(Long authorId);
    /** Finds questions in the requested lifecycle state. */
    List<QuestionRecord> findAllByStatus(QuestionStatus status);
    /** Finds published questions linked to a competency. */
    List<QuestionRecord> findDistinctByCompetencies_IdAndStatus(Long competencyId, QuestionStatus status);
}
