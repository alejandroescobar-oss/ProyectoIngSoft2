package com.example.questionbank.infrastructure.jpa.repository;

import com.example.questionbank.infrastructure.jpa.record.ReviewRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/** Spring Data implementation detail for review history. */
public interface SpringReviewDataRepository extends JpaRepository<ReviewRecord, Long> { }
