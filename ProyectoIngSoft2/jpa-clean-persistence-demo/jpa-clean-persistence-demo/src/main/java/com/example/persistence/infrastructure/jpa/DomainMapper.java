package com.example.persistence.infrastructure.jpa;

import com.example.persistence.domain.*;
import com.example.persistence.infrastructure.jpa.record.*;
import java.util.stream.Collectors;

/** Converts between technology-independent domain objects and JPA records. */
public final class DomainMapper {
    private DomainMapper() {}

    /** Converts a JPA student record to a domain student. */
    public static Student toDomain(StudentRecord record) {
        Student student = new Student(record.getId(), record.getFullName(), record.getEmail(), record.getStudentNumber());
        record.getCourses().forEach(course -> student.addCourseId(course.getId()));
        return student;
    }
    /** Converts a domain student to a new JPA record. */
    public static StudentRecord toRecord(Student student) {
        return new StudentRecord(student.getFullName(), student.getEmail(), student.getStudentNumber());
    }
    /** Converts a course record to its domain representation. */
    public static Course toDomain(CourseRecord record) {
        return new Course(record.getId(), record.getTitle(), record.getDepartment().getId());
    }
    /** Converts a department record to its domain representation. */
    public static Department toDomain(DepartmentRecord record) {
        return new Department(record.getId(), record.getName());
    }
}
