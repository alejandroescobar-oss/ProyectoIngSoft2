package com.example.questionbank;

import com.example.questionbank.application.CatalogService;
import com.example.questionbank.application.QuestionService;
import com.example.questionbank.application.QuizService;
import com.example.questionbank.application.ReviewService;
import com.example.questionbank.application.ResourceNotFoundException;
import com.example.questionbank.domain.model.*;
import com.example.questionbank.infrastructure.jpa.repository.SpringReviewDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

/** Integration checks for JPA mappings and question workflow rules using H2. */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
class QuestionBankPersistenceTest {
    @Autowired private CatalogService catalog;
    @Autowired private QuestionService questions;
    @Autowired private ReviewService reviews;
    @Autowired private QuizService quizzes;
    @Autowired private SpringReviewDataRepository reviewRecords;

    private Long authorId;
    private Long reviewerId;
    private Long teacherId;
    private Long topicId;
    private Long competencyId;

    @BeforeEach
    void setUp() {
        authorId = catalog.createUser("Question Author", "author@example.com", Role.AUTHOR).id();
        reviewerId = catalog.createUser("Question Reviewer", "reviewer@example.com", Role.REVIEWER).id();
        teacherId = catalog.createUser("Course Teacher", "teacher@example.com", Role.TEACHER).id();
        topicId = catalog.createTopic("Software Architecture").id();
        competencyId = catalog.createCompetency("Solution Design").id();
    }

    @Test
    void persistsQuestionInheritanceAssociationsMetadataAndReviewLifecycle() {
        MultipleChoiceQuestion created = (MultipleChoiceQuestion) questions.createMultipleChoice(
                "Which design separates concerns?", Difficulty.MEDIUM, authorId, topicId, Set.of(competencyId),
                new QuestionMetadata("ANALYSIS", 3, "Reviewed for clarity"), validOptions());

        MultipleChoiceQuestion loaded = assertInstanceOf(MultipleChoiceQuestion.class, questions.get(created.getId()));
        assertEquals(5, loaded.getOptions().size());
        assertEquals(1, loaded.getOptions().stream().filter(AnswerOption::correct).count());
        assertEquals(Set.of(competencyId), loaded.getCompetencyIds());
        assertEquals("ANALYSIS", loaded.getMetadata().cognitiveLevel());

        assertEquals(QuestionStatus.PENDING_REVIEW, questions.submitForReview(created.getId()).getStatus());
        assertEquals(QuestionStatus.IN_REVIEW, questions.startReview(created.getId(), reviewerId).getStatus());
        assertEquals(QuestionStatus.APPROVED,
                reviews.decide(created.getId(), reviewerId, ReviewDecision.APPROVED, "Clear and relevant").getStatus());
        assertEquals(1, reviewRecords.count());
        assertEquals(QuestionStatus.PUBLISHED, questions.publish(created.getId()).getStatus());
        assertEquals(1, questions.listPublishedByCompetency(competencyId).size());
    }

    @Test
    void rejectsInvalidDraftAndPreventsUnpublishedQuestionFromEnteringQuiz() {
        Question draft = questions.createMultipleChoice("Incomplete question", Difficulty.EASY, authorId,
                topicId, Set.of(competencyId), null, validOptions().subList(0, 4));

        assertThrows(IllegalStateException.class, () -> questions.submitForReview(draft.getId()));
        assertThrows(IllegalStateException.class, () -> quizzes.create("Practice quiz", teacherId, List.of(draft.getId())));
        assertEquals(QuestionStatus.DRAFT, questions.get(draft.getId()).getStatus());
    }

    @Test
    void createsQuizOnlyAfterQuestionIsPublished() {
        Question question = questions.createTrueFalse("A JPA record is a domain object.", Difficulty.EASY,
                authorId, topicId, Set.of(competencyId), null, false);
        assertThrows(IllegalStateException.class, () -> quizzes.create("Practice quiz", teacherId, List.of(question.getId())));

        questions.submitForReview(question.getId());
        questions.startReview(question.getId(), reviewerId);
        reviews.decide(question.getId(), reviewerId, ReviewDecision.APPROVED, "Approved");
        questions.publish(question.getId());
        Quiz quiz = quizzes.create("Practice quiz", teacherId, List.of(question.getId()));

        assertEquals(List.of(question.getId()), quiz.questionIds());
        assertEquals(1, quizzes.list().size());
    }

    @Test
    void rejectedQuestionReturnsToDraftAndCanBeSubmittedAgain() {
        Question question = questions.createTrueFalse("A repository hides storage details.", Difficulty.EASY,
                authorId, topicId, Set.of(competencyId), null, true);
        questions.submitForReview(question.getId());
        questions.startReview(question.getId(), reviewerId);

        Question rejected = reviews.decide(question.getId(), reviewerId, ReviewDecision.REJECTED, "Clarify wording");
        assertEquals(QuestionStatus.DRAFT, rejected.getStatus());
        assertEquals(1, reviewRecords.count());
        assertEquals(QuestionStatus.PENDING_REVIEW, questions.submitForReview(question.getId()).getStatus());
    }

    @Test
    void refusesQuestionAssociationsThatDoNotExist() {
        assertThrows(ResourceNotFoundException.class, () -> questions.createTrueFalse("Question text",
                Difficulty.EASY, 999L, topicId, Set.of(competencyId), null, true));
        assertThrows(ResourceNotFoundException.class, () -> questions.createTrueFalse("Question text",
                Difficulty.EASY, authorId, 999L, Set.of(competencyId), null, true));
        assertThrows(ResourceNotFoundException.class, () -> questions.createTrueFalse("Question text",
                Difficulty.EASY, authorId, topicId, Set.of(999L), null, true));
    }

    private static List<AnswerOption> validOptions() {
        return List.of(new AnswerOption(null, "Separation of concerns", true),
                new AnswerOption(null, "Global mutable state", false),
                new AnswerOption(null, "Tight coupling", false),
                new AnswerOption(null, "Duplicated logic", false),
                new AnswerOption(null, "Hidden dependencies", false));
    }
}
