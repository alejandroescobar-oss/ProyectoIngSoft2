package com.example.persistence.infrastructure.jpa.record;

import jakarta.persistence.*;

/** JPA record for courses; many courses may refer to one department. */
@Entity
@Table(name = "courses")
public class CourseRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false) private String title;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "department_id", nullable = false)
    private DepartmentRecord department;
    protected CourseRecord() {}
    public CourseRecord(String title, DepartmentRecord department) { this.title = title; this.department = department; }
    /** @return course ID */
    public Long getId() { return id; }
    /** @return course title */
    public String getTitle() { return title; }
    /** @return owning department */
    public DepartmentRecord getDepartment() { return department; }
    /** Sets the owning side of the many-to-one relationship. @param department department record */
    public void setDepartment(DepartmentRecord department) { this.department = department; }
}
