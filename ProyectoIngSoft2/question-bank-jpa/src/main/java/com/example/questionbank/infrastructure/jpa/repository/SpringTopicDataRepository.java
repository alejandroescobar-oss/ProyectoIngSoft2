package com.example.questionbank.infrastructure.jpa.repository;

import com.example.questionbank.infrastructure.jpa.record.TopicRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/** Spring Data implementation detail for topics. */
public interface SpringTopicDataRepository extends JpaRepository<TopicRecord, Long> { }
