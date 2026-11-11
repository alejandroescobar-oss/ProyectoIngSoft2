package com.example.persistence.infrastructure.jpa.repository;

import com.example.persistence.infrastructure.jpa.record.DepartmentRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/** Spring Data implementation detail used by the department repository adapter. */
public interface SpringDepartmentDataRepository extends JpaRepository<DepartmentRecord, Long> { }
