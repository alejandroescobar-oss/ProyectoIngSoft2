package com.example.questionbank.application;

import com.example.questionbank.domain.model.Question;
import com.example.questionbank.domain.model.Role;
import com.example.questionbank.domain.model.User;
import com.example.questionbank.domain.model.Review;
import com.example.questionbank.domain.model.ReviewDecision;
import com.example.questionbank.domain.repository.QuestionRepository;
import com.example.questionbank.domain.repository.ReviewRepository;
import com.example.questionbank.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;

/** Application use case for reviewer decisions. */
@Service
public class ReviewService {
    private final QuestionRepository questions;
    private final UserRepository users;
    private final ReviewRepository reviews;

    /** Injects question, user, and review repository contracts. */
    public ReviewService(QuestionRepository questions, UserRepository users, ReviewRepository reviews) {
        this.questions = questions;
        this.users = users;
        this.reviews = reviews;
    }
    /** Approves or rejects a question currently in review. */
    @Transactional
    public Question decide(Long questionId, Long reviewerId, ReviewDecision decision, String comments) {
        User reviewer = users.findById(reviewerId)
                .orElseThrow(() -> new ResourceNotFoundException("User " + reviewerId + " was not found"));
        if (reviewer.role() != Role.REVIEWER) throw new IllegalArgumentException("User must have role REVIEWER");
        Question question = questions.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question " + questionId + " was not found"));
        if (decision == ReviewDecision.APPROVED) question.approve();
        else if (decision == ReviewDecision.REJECTED) question.reject();
        else throw new IllegalArgumentException("Review decision is required");
        Question saved = questions.save(question);
        reviews.save(new Review(null, questionId, reviewerId, decision, comments, Instant.now()));
        return saved;
    }
}
