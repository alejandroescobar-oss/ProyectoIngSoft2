package com.example.persistence.domain;

/** An instructor, specialized from Person. */
public class Instructor extends Person {
    private String office;
    public Instructor() {}
    public Instructor(Long id, String fullName, String email, String office) {
        super(id, fullName, email); this.office = office;
    }
    /** @return instructor office */
    public String getOffice() { return office; }
}
