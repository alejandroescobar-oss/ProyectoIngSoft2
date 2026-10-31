package com.example.questionbank.infrastructure.jpa;

import com.example.questionbank.domain.model.Question;
import com.example.questionbank.domain.model.QuestionMetadata;
import com.example.questionbank.domain.model.QuestionStatus;
import com.example.questionbank.domain.repository.QuestionRepository;
import com.example.questionbank.infrastructure.jpa.record.*;
import com.example.questionbank.infrastructure.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/** JPA implementation of the technology-independent question repository. */
@Repository
public class QuestionRepositoryJpa implements QuestionRepository {
    private final SpringQuestionDataRepository questions;
    private final SpringUserDataRepository users;
    private final SpringTopicDataRepository topics;
    private final SpringCompetencyDataRepository competencies;

    /** Creates the adapter and its required Spring Data dependencies. */
    public QuestionRepositoryJpa(SpringQuestionDataRepository questions, SpringUserDataRepository users,
                                 SpringTopicDataRepository topics, SpringCompetencyDataRepository competencies) {
        this.questions = questions;
        this.users = users;
        this.topics = topics;
        this.competencies = competencies;
    }

    @Override
    @Transactional
    public Question save(Question question) {
        if (question.getId() != null) {
            QuestionRecord existing = questions.findById(question.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Question " + question.getId() + " was not found"));
            boolean matchingType = question instanceof com.example.questionbank.domain.model.MultipleChoiceQuestion
                    ? existing instanceof MultipleChoiceQuestionRecord
                    : question instanceof com.example.questionbank.domain.model.TrueFalseQuestion
                        && existing instanceof TrueFalseQuestionRecord;
            if (!matchingType) throw new IllegalArgumentException("A persisted question cannot change its type");
            existing.setStatement(question.getStatement());
            existing.setDifficulty(question.getDifficulty());
            existing.setStatus(question.getStatus());
            existing.setTopic(topics.getReferenceById(question.getTopicId()));
            existing.setCompetencies(competencies.findAllById(question.getCompetencyIds())
                    .stream().collect(Collectors.toSet()));
            QuestionMetadata metadata = question.getMetadata();
            QuestionMetadataRecord metadataRecord = existing.getMetadata();
            if (metadata == null) {
                existing.setMetadata(null);
            } else if (metadataRecord == null) {
                existing.setMetadata(new QuestionMetadataRecord(metadata.cognitiveLevel(),
                        metadata.estimatedMinutes(), metadata.editorialNotes()));
            } else {
                metadataRecord.setCognitiveLevel(metadata.cognitiveLevel());
                metadataRecord.setEstimatedMinutes(metadata.estimatedMinutes());
                metadataRecord.setEditorialNotes(metadata.editorialNotes());
            }
            return QuestionMapper.toDomain(existing);
        }
        UserRecord author = users.getReferenceById(question.getAuthorId());
        TopicRecord topic = topics.getReferenceById(question.getTopicId());
        Set<CompetencyRecord> competencyRecords = competencies.findAllById(question.getCompetencyIds())
                .stream().collect(Collectors.toSet());
        QuestionRecord saved = questions.save(QuestionMapper.toRecord(question, author, topic, competencyRecords));
        return QuestionMapper.toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Question> findById(Long id) { return questions.findById(id).map(QuestionMapper::toDomain); }
    @Override
    @Transactional(readOnly = true)
    public List<Question> findAll() { return questions.findAll().stream().map(QuestionMapper::toDomain).toList(); }
    @Override
    @Transactional(readOnly = true)
    public List<Question> findByAuthorId(Long authorId) {
        return questions.findAllByAuthor_Id(authorId).stream().map(QuestionMapper::toDomain).toList();
    }
    @Override
    @Transactional(readOnly = true)
    public List<Question> findByStatus(QuestionStatus status) {
        return questions.findAllByStatus(status).stream().map(QuestionMapper::toDomain).toList();
    }
    @Override
    @Transactional(readOnly = true)
    public List<Question> findPublishedByCompetency(Long competencyId) {
        return questions.findDistinctByCompetencies_IdAndStatus(competencyId, QuestionStatus.PUBLISHED)
                .stream().map(QuestionMapper::toDomain).toList();
    }
}
