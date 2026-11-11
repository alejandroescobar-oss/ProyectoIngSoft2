package com.example.questionbank.application;

import com.example.questionbank.domain.model.*;
import com.example.questionbank.domain.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;

/** Coordinates question creation, classification, queries, and lifecycle rules. */
@Service
@Transactional
public class QuestionService {
    private final QuestionRepository questions;
    private final UserRepository users;
    private final TopicRepository topics;
    private final CompetencyRepository competencies;

    /** Injects repository contracts rather than Spring Data interfaces. */
    public QuestionService(QuestionRepository questions, UserRepository users,
                           TopicRepository topics, CompetencyRepository competencies) {
        this.questions = questions;
        this.users = users;
        this.topics = topics;
        this.competencies = competencies;
    }

    /** Creates a multiple-choice question in draft state. */
    public Question createMultipleChoice(String statement, Difficulty difficulty, Long authorId, Long topicId,
                                         Set<Long> competencyIds, QuestionMetadata metadata,
                                         List<AnswerOption> options) {
        requireUser(authorId, Role.AUTHOR);
        requireTopic(topicId);
        requireCompetencies(competencyIds);
        return questions.save(new MultipleChoiceQuestion(null, statement, difficulty, QuestionStatus.DRAFT,
                authorId, topicId, competencyIds, metadata, options));
    }

    /** Creates a true/false question in draft state. */
    public Question createTrueFalse(String statement, Difficulty difficulty, Long authorId, Long topicId,
                                    Set<Long> competencyIds, QuestionMetadata metadata, boolean expectedAnswer) {
        requireUser(authorId, Role.AUTHOR);
        requireTopic(topicId);
        requireCompetencies(competencyIds);
        return questions.save(new TrueFalseQuestion(null, statement, difficulty, QuestionStatus.DRAFT,
                authorId, topicId, competencyIds, metadata, expectedAnswer));
    }

    /** Returns one question or signals that it does not exist. */
    @Transactional(readOnly = true)
    public Question get(Long id) { return findQuestion(id); }
    /** Lists all questions, or filters by author or status when supplied. */
    @Transactional(readOnly = true)
    public List<Question> list(Long authorId, QuestionStatus status) {
        if (authorId != null) {
            List<Question> authored = questions.findByAuthorId(authorId);
            return status == null ? authored : authored.stream().filter(question -> question.getStatus() == status).toList();
        }
        if (status != null) return questions.findByStatus(status);
        return questions.findAll();
    }
    /** Lists published questions for a competency. */
    @Transactional(readOnly = true)
    public List<Question> listPublishedByCompetency(Long competencyId) {
        if (competencies.findById(competencyId).isEmpty()) {
            throw new ResourceNotFoundException("Competency " + competencyId + " was not found");
        }
        return questions.findPublishedByCompetency(competencyId);
    }

    /** Validates draft content before placing the question in the review queue. */
    public Question submitForReview(Long id) {
        Question question = findQuestion(id);
        if (question.getCompetencyIds().isEmpty()) {
            throw new IllegalStateException("At least one competency is required before review");
        }
        if (question instanceof MultipleChoiceQuestion multipleChoice) {
            long correctAnswers = multipleChoice.getOptions().stream().filter(AnswerOption::correct).count();
            if (multipleChoice.getOptions().size() != 5 || correctAnswers != 1) {
                throw new IllegalStateException("A multiple-choice question needs five options and one correct answer");
            }
        }
        question.submitForReview();
        return questions.save(question);
    }

    /** Moves a question from the review queue to active review. */
    public Question startReview(Long id, Long reviewerId) {
        requireUser(reviewerId, Role.REVIEWER);
        Question question = findQuestion(id);
        question.startReview();
        return questions.save(question);
    }

    /** Publishes an approved question. */
    public Question publish(Long id) {
        Question question = findQuestion(id);
        question.publish();
        return questions.save(question);
    }

    /** Archives a published question. */
    public Question archive(Long id) {
        Question question = findQuestion(id);
        question.archive();
        return questions.save(question);
    }

    private Question findQuestion(Long id) {
        return questions.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question " + id + " was not found"));
    }
    private void requireUser(Long id, Role expectedRole) {
        User user = users.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User " + id + " was not found"));
        if (user.role() != expectedRole) throw new IllegalArgumentException("User must have role " + expectedRole);
    }
    private void requireTopic(Long id) {
        if (topics.findById(id).isEmpty()) throw new ResourceNotFoundException("Topic " + id + " was not found");
    }
    private void requireCompetencies(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) throw new IllegalArgumentException("At least one competency is required");
        long existing = ids.stream().filter(id -> competencies.findById(id).isPresent()).count();
        if (existing != ids.size()) throw new ResourceNotFoundException("One or more competencies were not found");
    }
}
