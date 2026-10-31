package com.example.questionbank.infrastructure.jpa;

import com.example.questionbank.domain.model.Topic;
import com.example.questionbank.domain.repository.TopicRepository;
import com.example.questionbank.infrastructure.jpa.record.TopicRecord;
import com.example.questionbank.infrastructure.jpa.repository.SpringTopicDataRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/** JPA implementation of the topic repository contract. */
@Repository
public class TopicRepositoryJpa implements TopicRepository {
    private final SpringTopicDataRepository data;
    /** Creates the adapter around the Spring Data repository. */
    public TopicRepositoryJpa(SpringTopicDataRepository data) { this.data = data; }
    @Override public Topic save(Topic topic) {
        TopicRecord saved = data.save(new TopicRecord(topic.name()));
        return new Topic(saved.getId(), saved.getName());
    }
    @Override public Optional<Topic> findById(Long id) {
        return data.findById(id).map(record -> new Topic(record.getId(), record.getName()));
    }
}
