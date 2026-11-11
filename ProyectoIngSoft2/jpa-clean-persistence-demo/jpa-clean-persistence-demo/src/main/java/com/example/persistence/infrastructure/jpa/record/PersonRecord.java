package com.example.persistence.infrastructure.jpa.record;

import jakarta.persistence.*;

/** JPA-specific representation of Person; JPA annotations stay outside the domain. */
@Entity
@Table(name = "people")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class PersonRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false) private String fullName;
    @Column(nullable = false, unique = true) private String email;
    protected PersonRecord() {}
    protected PersonRecord(String fullName, String email) { this.fullName = fullName; this.email = email; }
    /** @return database identity */
    public Long getId() { return id; }
    /** @return full name */
    public String getFullName() { return fullName; }
    /** @return email */
    public String getEmail() { return email; }
}
