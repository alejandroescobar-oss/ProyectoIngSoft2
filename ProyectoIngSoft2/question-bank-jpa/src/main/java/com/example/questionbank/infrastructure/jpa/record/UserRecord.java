package com.example.questionbank.infrastructure.jpa.record;

import com.example.questionbank.domain.model.Role;
import jakarta.persistence.*;

/** JPA representation of a question-bank user. */
@Entity
@Table(name = "qb_users")
public class UserRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String fullName;
    @Column(nullable = false, unique = true)
    private String email;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private Role role;

    protected UserRecord() { }
    /** Creates a persistence record from user fields. */
    public UserRecord(String fullName, String email, Role role) {
        this.fullName = fullName;
        this.email = email;
        this.role = role;
    }
    /** @return database identifier */
    public Long getId() { return id; }
    /** @return full name */
    public String getFullName() { return fullName; }
    /** @return email address */
    public String getEmail() { return email; }
    /** @return assigned role */
    public Role getRole() { return role; }
}
