package com.example.persistence.domain.repository;
import com.example.persistence.domain.Department;
import java.util.List;
import java.util.Optional;

/** Persistence contract for departments. */
public interface DepartmentRepository {
    /** Stores a department. */
    Department save(Department department);
    /** Finds a department by identity. */
    Optional<Department> findById(Long id);
    /** Lists departments. */
    List<Department> findAll();
}
