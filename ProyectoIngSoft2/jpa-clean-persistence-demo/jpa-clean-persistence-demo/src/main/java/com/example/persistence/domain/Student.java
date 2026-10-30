package com.example.persistence.domain;

import java.util.ArrayList;
import java.util.List;

/** A student, specialized from the technology-independent Person model. */
public class Student extends Person {
    private String studentNumber;
    private final List<Long> courseIds = new ArrayList<>();

    public Student() {}
    public Student(Long id, String fullName, String email, String studentNumber) {
        super(id, fullName, email); this.studentNumber = studentNumber;
    }
    /** @return university student number */
    public String getStudentNumber() { return studentNumber; }
    /** @return IDs of courses in which this student is enrolled */
    public List<Long> getCourseIds() { return List.copyOf(courseIds); }
    /** Adds a course identity without exposing persistence entities. @param courseId course identifier */
    public void addCourseId(Long courseId) { if (!courseIds.contains(courseId)) courseIds.add(courseId); }
}
