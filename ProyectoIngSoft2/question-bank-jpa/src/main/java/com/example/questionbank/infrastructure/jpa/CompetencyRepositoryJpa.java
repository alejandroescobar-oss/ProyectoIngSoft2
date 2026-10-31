package com.example.questionbank.infrastructure.jpa;

import com.example.questionbank.domain.model.Competency;
import com.example.questionbank.domain.repository.CompetencyRepository;
import com.example.questionbank.infrastructure.jpa.record.CompetencyRecord;
import com.example.questionbank.infrastructure.jpa.repository.SpringCompetencyDataRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/** JPA implementation of the competency repository contract. */
@Repository
public class CompetencyRepositoryJpa implements CompetencyRepository {
    private final SpringCompetencyDataRepository data;
    /** Creates the adapter around the Spring Data repository. */
    public CompetencyRepositoryJpa(SpringCompetencyDataRepository data) { this.data = data; }
    @Override public Competency save(Competency competency) {
        CompetencyRecord saved = data.save(new CompetencyRecord(competency.name()));
        return new Competency(saved.getId(), saved.getName());
    }
    @Override public Optional<Competency> findById(Long id) {
        return data.findById(id).map(record -> new Competency(record.getId(), record.getName()));
    }
}
