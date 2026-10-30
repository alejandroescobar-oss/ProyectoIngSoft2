package com.example.questionbank.api.requests;

import jakarta.validation.constraints.NotBlank;

/** HTTP payload for creating a named topic or competency. */
public record CreateNameRequest(@NotBlank String name) { }
