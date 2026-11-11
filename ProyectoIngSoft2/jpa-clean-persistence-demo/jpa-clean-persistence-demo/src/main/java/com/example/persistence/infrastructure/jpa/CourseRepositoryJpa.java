package com.example.persistence.infrastructure.jpa;

import com.example.persistence.domain.Course;
import com.example.persistence.domain.repository.CourseRepository;
import com.example.persistence.infrastructure.jpa.record.CourseRecord;
import com.example.persistence.infrastructure.jpa.repository.SpringCourseDataRepository;
import com.example.persistence.infrastructure.jpa.repository.SpringDepartmentDataRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/** Concrete JPA implementation of CourseRepository. */
@Repository
public class CourseRepositoryJpa implements CourseRepository {
    private final SpringCourseDataRepository courses;
    private final SpringDepartmentDataRepository departments;
    /** Creates the adapter. */
    public CourseRepositoryJpa(SpringCourseDataRepository courses, SpringDepartmentDataRepository departments) {
        this.courses = courses; this.departments = departments;
    }
    @Override public Course save(Course course) {
        var department = departments.findById(course.getDepartmentId())
            .orElseThrow(() -> new IllegalArgumentException("Department not found: " + course.getDepartmentId()));
        CourseRecord record = new CourseRecord(course.getTitle(), department);
        return DomainMapper.toDomain(courses.save(record));
    }
    @Override public Optional<Course> findById(Long id) { return courses.findById(id).map(DomainMapper::toDomain); }
    @Override public List<Course> findAll() { return courses.findAll().stream().map(DomainMapper::toDomain).toList(); }
}
