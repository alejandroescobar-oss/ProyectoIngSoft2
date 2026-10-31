package com.example.questionbank.domain;

import com.example.questionbank.domain.model.Difficulty;
import com.example.questionbank.domain.model.QuestionStatus;
import com.example.questionbank.domain.model.TrueFalseQuestion;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

/** Verifies lifecycle rules using only domain objects, without starting JPA. */
public class QuestionLifecycleTest {
    @Test
    public void allowsReviewRejectionAndResubmissionButRejectsInvalidTransitions() {
        TrueFalseQuestion question = new TrueFalseQuestion(null, "Example statement", Difficulty.EASY,
                QuestionStatus.DRAFT, 1L, 2L, Set.of(3L), null, true);

        question.submitForReview();
        question.startReview();
        question.reject();
        assertEquals(QuestionStatus.DRAFT, question.getStatus());
        question.submitForReview();
        assertEquals(QuestionStatus.PENDING_REVIEW, question.getStatus());
        assertThrows(IllegalStateException.class, question::publish);
    }

    @Test
    public void restrictsDraftReclassificationToItsOriginalAuthor() {
        TrueFalseQuestion question = new TrueFalseQuestion(null, "Example statement", Difficulty.EASY,
                QuestionStatus.DRAFT, 1L, 2L, Set.of(3L), null, true);

        assertThrows(IllegalArgumentException.class, () -> question.reclassify(null, 4L, Set.of(5L)));
        assertThrows(IllegalArgumentException.class, () -> question.reclassify(9L, 4L, Set.of(5L)));
        question.reclassify(1L, 4L, Set.of(5L));

        assertEquals(4L, question.getTopicId());
        assertEquals(Set.of(5L), question.getCompetencyIds());
    }
}
