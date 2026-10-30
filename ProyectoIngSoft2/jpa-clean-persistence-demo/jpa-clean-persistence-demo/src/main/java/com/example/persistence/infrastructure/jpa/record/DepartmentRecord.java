package com.example.persistence.infrastructure.jpa.record;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/** JPA record for departments; one department owns many courses. */
@Entity
@Table(name = "departments")
public class DepartmentRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true) private String name;
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseRecord> courses = new ArrayList<>();
    protected DepartmentRecord() {}
    public DepartmentRecord(String name) { this.name = name; }
    /** @return department ID */
    public Long getId() { return id; }
    /** @return department name */
    public String getName() { return name; }
    /** @return courses belonging to this department */
    public List<CourseRecord> getCourses() { return courses; }
    /** Adds a course and maintains both sides of the relationship. @param course course record */
    public void addCourse(CourseRecord course) { courses.add(course); course.setDepartment(this); }
}
