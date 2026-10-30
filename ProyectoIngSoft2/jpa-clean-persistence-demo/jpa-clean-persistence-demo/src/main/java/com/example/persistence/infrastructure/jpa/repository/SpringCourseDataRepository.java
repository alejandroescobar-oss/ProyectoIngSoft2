package com.example.persistence.infrastructure.jpa.repository;
import com.example.persistence.infrastructure.jpa.record.CourseRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/** Spring Data adapter used internally by the course repository implementation. */
public interface SpringCourseDataRepository extends JpaRepository<CourseRecord, Long> {}
