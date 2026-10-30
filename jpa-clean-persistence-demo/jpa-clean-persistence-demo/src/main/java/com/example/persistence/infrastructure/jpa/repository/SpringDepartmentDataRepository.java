package com.example.persistence.infrastructure.jpa.repository;
import com.example.persistence.infrastructure.jpa.record.DepartmentRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/** Spring Data adapter used internally by the department repository implementation. */
public interface SpringDepartmentDataRepository extends JpaRepository<DepartmentRecord, Long> {}
