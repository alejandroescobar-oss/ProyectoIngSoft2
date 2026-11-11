package com.example.questionbank.api.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/** HTTP payload for creating a quiz. */
public record CreateQuizRequest(@NotBlank String title, @NotNull Long teacherId,
                                @NotEmpty List<@NotNull Long> questionIds) { }
