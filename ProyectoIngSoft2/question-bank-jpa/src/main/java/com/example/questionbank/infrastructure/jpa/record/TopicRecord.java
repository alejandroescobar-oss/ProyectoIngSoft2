package com.example.questionbank.infrastructure.jpa.record;

import jakarta.persistence.*;

/** JPA representation of an academic topic. */
@Entity
@Table(name = "topics")
public class TopicRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;

    protected TopicRecord() { }
    /** Creates a topic record. */
    public TopicRecord(String name) { this.name = name; }
    /** @return database identifier */
    public Long getId() { return id; }
    /** @return topic name */
    public String getName() { return name; }
}
