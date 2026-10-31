package com.example.questionbank.api.requests;

import com.example.questionbank.domain.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/** HTTP payload for registering a system user. */
public record CreateUserRequest(@NotBlank String fullName, @NotBlank @Email String email,
                                @NotNull Role role) { }
