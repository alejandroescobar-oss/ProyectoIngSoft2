package com.example.persistence.domain;

/** Technology-independent base class for people in the school domain. */
public abstract class Person {
    private Long id;
    private String fullName;
    private String email;

    protected Person() {}
    protected Person(Long id, String fullName, String email) {
        this.id = id; this.fullName = fullName; this.email = email;
    }
    /** @return persistent identity assigned by the repository */
    public Long getId() { return id; }
    /** Sets the identity. @param id identifier */
    public void setId(Long id) { this.id = id; }
    /** @return person's full name */
    public String getFullName() { return fullName; }
    /** @return person's email address */
    public String getEmail() { return email; }
}
