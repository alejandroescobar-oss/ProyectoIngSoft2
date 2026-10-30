package com.example.persistence.domain.repository;
import com.example.persistence.domain.Course;
import java.util.List;
import java.util.Optional;

/** Persistence contract for courses, independent of storage technology. */
public interface CourseRepository {
    /** Stores a course. */
    Course save(Course course);
    /** Finds a course by identity. */
    Optional<Course> findById(Long id);
    /** Lists all courses. */
    List<Course> findAll();
}
