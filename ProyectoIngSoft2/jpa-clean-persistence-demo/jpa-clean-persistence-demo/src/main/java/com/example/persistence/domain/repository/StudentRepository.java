package com.example.persistence.domain.repository;
import com.example.persistence.domain.Student;
import java.util.List;
import java.util.Optional;

/** Persistence contract expressed without Spring or JPA types. */
public interface StudentRepository {
    /** Stores a student and returns the saved domain object. */
    Student save(Student student);
    /** Finds a student by identity. */
    Optional<Student> findById(Long id);
    /** Lists all students. */
    List<Student> findAll();
    /** Enrolls a student in a course and returns the updated student. */
    Student enrollInCourse(Long studentId, Long courseId);
}
