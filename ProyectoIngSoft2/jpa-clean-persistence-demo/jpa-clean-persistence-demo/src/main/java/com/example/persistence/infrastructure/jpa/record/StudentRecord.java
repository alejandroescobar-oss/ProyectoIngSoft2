package com.example.persistence.infrastructure.jpa.record;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

/** JPA record for students, including the many-to-many enrollment mapping. */
@Entity
@Table(name = "students")
public class StudentRecord extends PersonRecord {
    @Column(nullable = false, unique = true) private String studentNumber;
    @ManyToMany
    @JoinTable(name = "student_courses",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Set<CourseRecord> courses = new HashSet<>();
    protected StudentRecord() {}
    public StudentRecord(String fullName, String email, String studentNumber) {
        super(fullName, email); this.studentNumber = studentNumber;
    }
    /** @return student number */
    public String getStudentNumber() { return studentNumber; }
    /** @return enrolled courses */
    public Set<CourseRecord> getCourses() { return courses; }
    /** Adds a course to the enrollment association. @param course course record */
    public void addCourse(CourseRecord course) { courses.add(course); }
}
