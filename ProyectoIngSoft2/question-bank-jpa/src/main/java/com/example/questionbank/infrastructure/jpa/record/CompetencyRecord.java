package com.example.questionbank.infrastructure.jpa.record;

import jakarta.persistence.*;

/** JPA representation of an academic competency. */
@Entity
@Table(name = "competencies")
public class CompetencyRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;

    protected CompetencyRecord() { }
    /** Creates a competency record. */
    public CompetencyRecord(String name) { this.name = name; }
    /** @return database identifier */
    public Long getId() { return id; }
    /** @return competency name */
    public String getName() { return name; }
}
