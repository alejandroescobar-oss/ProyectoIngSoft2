package com.example.questionbank.application;

import com.example.questionbank.domain.model.*;
import com.example.questionbank.domain.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Application use cases for users and academic classification catalogs. */
@Service
@Transactional
public class CatalogService {
    private final UserRepository users;
    private final TopicRepository topics;
    private final CompetencyRepository competencies;

    /** Injects technology-independent repository contracts. */
    public CatalogService(UserRepository users, TopicRepository topics, CompetencyRepository competencies) {
        this.users = users;
        this.topics = topics;
        this.competencies = competencies;
    }
    /** Registers a user. */
    public User createUser(String name, String email, Role role) {
        return users.save(new User(null, name, email, role));
    }
    /** Registers a topic. */
    public Topic createTopic(String name) { return topics.save(new Topic(null, name)); }
    /** Registers a competency. */
    public Competency createCompetency(String name) { return competencies.save(new Competency(null, name)); }
}
