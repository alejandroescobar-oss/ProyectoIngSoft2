package com.example.questionbank.domain.repository;

import com.example.questionbank.domain.model.Topic;
import java.util.Optional;

/** Technology-independent persistence contract for topics. */
public interface TopicRepository {
    /** Saves a topic. */
    Topic save(Topic topic);
    /** Finds a topic by identifier. */
    Optional<Topic> findById(Long id);
}
