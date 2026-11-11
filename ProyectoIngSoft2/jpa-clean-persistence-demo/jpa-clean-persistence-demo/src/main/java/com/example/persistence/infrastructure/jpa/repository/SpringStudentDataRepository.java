package com.example.persistence.infrastructure.jpa.repository;
import com.example.persistence.infrastructure.jpa.record.StudentRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/** Spring Data adapter used internally by the repository implementation. */
public interface SpringStudentDataRepository extends JpaRepository<StudentRecord, Long> {}
