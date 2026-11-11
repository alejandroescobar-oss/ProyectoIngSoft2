package com.example.questionbank.domain.repository;

import com.example.questionbank.domain.model.Competency;
import java.util.Optional;

/** Technology-independent persistence contract for competencies. */
public interface CompetencyRepository {
    /** Saves a competency. */
    Competency save(Competency competency);
    /** Finds a competency by identifier. */
    Optional<Competency> findById(Long id);
}
