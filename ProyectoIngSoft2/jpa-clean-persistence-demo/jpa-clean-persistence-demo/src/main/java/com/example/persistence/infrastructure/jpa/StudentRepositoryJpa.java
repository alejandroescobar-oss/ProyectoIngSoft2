package com.example.persistence.infrastructure.jpa;

import com.example.persistence.domain.Student;
import com.example.persistence.domain.repository.StudentRepository;
import com.example.persistence.infrastructure.jpa.record.StudentRecord;
import com.example.persistence.infrastructure.jpa.repository.SpringStudentDataRepository;
import com.example.persistence.infrastructure.jpa.repository.SpringCourseDataRepository;
import com.example.persistence.infrastructure.jpa.record.CourseRecord;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/** Concrete JPA implementation of the domain StudentRepository contract. */
@Repository
public class StudentRepositoryJpa implements StudentRepository {
    private final SpringStudentDataRepository data;
    private final SpringCourseDataRepository courses;
    /** Creates the adapter. @param data Spring Data student repository @param courses Spring Data course repository */
    public StudentRepositoryJpa(SpringStudentDataRepository data, SpringCourseDataRepository courses) {
        this.data = data; this.courses = courses;
    }
    @Override public Student save(Student student) {
        StudentRecord saved = data.save(DomainMapper.toRecord(student));
        return DomainMapper.toDomain(saved);
    }
    @Override public Optional<Student> findById(Long id) {
        return data.findById(id).map(DomainMapper::toDomain);
    }
    @Override @Transactional
    public Student enrollInCourse(Long studentId, Long courseId) {
        StudentRecord student = data.findById(studentId)
            .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));
        CourseRecord course = courses.findById(courseId)
            .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));
        student.addCourse(course);
        return DomainMapper.toDomain(data.save(student));
    }
    @Override public List<Student> findAll() {
        return data.findAll().stream().map(DomainMapper::toDomain).toList();
    }
}
