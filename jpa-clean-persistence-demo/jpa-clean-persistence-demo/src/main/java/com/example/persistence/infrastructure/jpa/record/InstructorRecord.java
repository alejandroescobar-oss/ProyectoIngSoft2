package com.example.persistence.infrastructure.jpa.record;

import jakarta.persistence.*;

/** JPA record for instructors, a second subtype in the inheritance hierarchy. */
@Entity
@Table(name = "instructors")
public class InstructorRecord extends PersonRecord {
    private String office;
    protected InstructorRecord() {}
    /** Creates an instructor persistence record. */
    public InstructorRecord(String fullName, String email, String office) {
        super(fullName, email); this.office = office;
    }
    /** @return office location */
    public String getOffice() { return office; }
}
