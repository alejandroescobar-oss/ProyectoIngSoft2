package com.example.questionbank.domain.repository;

import com.example.questionbank.domain.model.Review;

/** Technology-independent contract for persisting reviewer decisions. */
public interface ReviewRepository {
    /** Records a decision for an existing question by an existing reviewer. */
    Review save(Review review);
}
