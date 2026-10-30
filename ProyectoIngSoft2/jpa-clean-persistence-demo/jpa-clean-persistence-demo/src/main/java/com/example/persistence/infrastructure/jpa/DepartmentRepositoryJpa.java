package com.example.persistence.infrastructure.jpa;

import com.example.persistence.domain.Department;
import com.example.persistence.domain.repository.DepartmentRepository;
import com.example.persistence.infrastructure.jpa.record.DepartmentRecord;
import com.example.persistence.infrastructure.jpa.repository.SpringDepartmentDataRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/** Concrete JPA implementation of DepartmentRepository. */
@Repository
public class DepartmentRepositoryJpa implements DepartmentRepository {
    private final SpringDepartmentDataRepository data;
    /** Creates the adapter. @param data Spring Data repository */
    public DepartmentRepositoryJpa(SpringDepartmentDataRepository data) { this.data = data; }
    @Override public Department save(Department department) {
        DepartmentRecord record = data.save(new DepartmentRecord(department.getName()));
        department.setId(record.getId());
        return DomainMapper.toDomain(record);
    }
    @Override public Optional<Department> findById(Long id) { return data.findById(id).map(DomainMapper::toDomain); }
    @Override public List<Department> findAll() { return data.findAll().stream().map(DomainMapper::toDomain).toList(); }
}
