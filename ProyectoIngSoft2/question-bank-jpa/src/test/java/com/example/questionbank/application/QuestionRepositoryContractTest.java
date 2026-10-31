package com.example.questionbank.application;

import com.example.questionbank.domain.model.*;
import com.example.questionbank.domain.repository.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import static org.junit.jupiter.api.Assertions.*;

/** Verifies application behavior using repository contracts without starting JPA. */
class QuestionRepositoryContractTest {
    @Test
    void createsAndQueriesQuestionsThroughTechnologyIndependentContracts() {
        InMemoryQuestionRepository questions = new InMemoryQuestionRepository();
        UserRepository users = new UserRepository() {
            private final User author = new User(1L, "Author", "author@example.com", Role.AUTHOR);
            @Override public User save(User user) { return user; }
            @Override public Optional<User> findById(Long id) {
                return id.equals(author.id()) ? Optional.of(author) : Optional.empty();
            }
            @Override public List<User> findAll() { return List.of(author); }
        };
        TopicRepository topics = new TopicRepository() {
            @Override public Topic save(Topic topic) { return topic; }
            @Override public Optional<Topic> findById(Long id) {
                return id.equals(2L) ? Optional.of(new Topic(2L, "Architecture")) : Optional.empty();
            }
        };
        CompetencyRepository competencies = new CompetencyRepository() {
            @Override public Competency save(Competency competency) { return competency; }
            @Override public Optional<Competency> findById(Long id) {
                return id.equals(3L) ? Optional.of(new Competency(3L, "Design")) : Optional.empty();
            }
        };
        QuestionService service = new QuestionService(questions, users, topics, competencies);

        Question created = service.createTrueFalse("A repository isolates persistence.", Difficulty.EASY,
                1L, 2L, Set.of(3L), null, true);

        assertNotNull(created.getId());
        assertEquals(created, service.get(created.getId()));
        assertEquals(List.of(created), service.list(1L, QuestionStatus.DRAFT));
        assertEquals(QuestionStatus.PENDING_REVIEW,
                service.submitForReview(created.getId()).getStatus());
    }

    private static final class InMemoryQuestionRepository implements QuestionRepository {
        private final AtomicLong ids = new AtomicLong();
        private final Map<Long, Question> stored = new HashMap<>();

        @Override
        public Question save(Question question) {
            Long id = question.getId() == null ? ids.incrementAndGet() : question.getId();
            Question persisted = new TrueFalseQuestion(id, question.getStatement(), question.getDifficulty(),
                    question.getStatus(), question.getAuthorId(), question.getTopicId(),
                    question.getCompetencyIds(), question.getMetadata(),
                    ((TrueFalseQuestion) question).isExpectedAnswer());
            stored.put(id, persisted);
            return persisted;
        }

        @Override public Optional<Question> findById(Long id) { return Optional.ofNullable(stored.get(id)); }
        @Override public List<Question> findAll() { return new ArrayList<>(stored.values()); }
        @Override public List<Question> findByAuthorId(Long authorId) {
            return stored.values().stream().filter(question -> question.getAuthorId().equals(authorId)).toList();
        }
        @Override public List<Question> findByStatus(QuestionStatus status) {
            return stored.values().stream().filter(question -> question.getStatus() == status).toList();
        }
        @Override public List<Question> findPublishedByCompetency(Long competencyId) {
            return stored.values().stream()
                    .filter(question -> question.getStatus() == QuestionStatus.PUBLISHED)
                    .filter(question -> question.getCompetencyIds().contains(competencyId))
                    .toList();
        }
    }
}
