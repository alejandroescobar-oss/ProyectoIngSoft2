package com.example.questionbank.infrastructure.jpa;

import com.example.questionbank.domain.model.Review;
import com.example.questionbank.domain.repository.ReviewRepository;
import com.example.questionbank.infrastructure.jpa.record.QuestionRecord;
import com.example.questionbank.infrastructure.jpa.record.ReviewRecord;
import com.example.questionbank.infrastructure.jpa.record.UserRecord;
import com.example.questionbank.infrastructure.jpa.repository.SpringQuestionDataRepository;
import com.example.questionbank.infrastructure.jpa.repository.SpringReviewDataRepository;
import com.example.questionbank.infrastructure.jpa.repository.SpringUserDataRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/** JPA implementation of reviewer decision persistence. */
@Repository
public class ReviewRepositoryJpa implements ReviewRepository {
    private final SpringReviewDataRepository reviews;
    private final SpringQuestionDataRepository questions;
    private final SpringUserDataRepository users;

    /** Creates the adapter and required data repositories. */
    public ReviewRepositoryJpa(SpringReviewDataRepository reviews, SpringQuestionDataRepository questions,
                               SpringUserDataRepository users) {
        this.reviews = reviews;
        this.questions = questions;
        this.users = users;
    }

    @Override
    @Transactional
    public Review save(Review review) {
        QuestionRecord question = questions.getReferenceById(review.questionId());
        UserRecord reviewer = users.getReferenceById(review.reviewerId());
        ReviewRecord saved = reviews.save(new ReviewRecord(question, reviewer, review.decision(), review.comments(),
                review.createdAt()));
        return new Review(saved.getId(), saved.getQuestion().getId(), saved.getReviewer().getId(),
                saved.getDecision(), saved.getComments(), saved.getCreatedAt());
    }
}
