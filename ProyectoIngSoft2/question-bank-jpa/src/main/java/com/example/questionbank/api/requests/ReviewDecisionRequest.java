package com.example.questionbank.api.requests;

import com.example.questionbank.domain.model.ReviewDecision;
import jakarta.validation.constraints.NotNull;

/** HTTP payload for a reviewer's decision. */
public record ReviewDecisionRequest(@NotNull Long reviewerId, @NotNull ReviewDecision decision, String comments) { }
