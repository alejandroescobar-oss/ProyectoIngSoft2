package com.example.persistence.domain;

/** Course domain object; departmentId represents its many-to-one association. */
public class Course {
    private Long id;
    private String title;
    private Long departmentId;
    public Course() {}
    public Course(Long id, String title, Long departmentId) {
        this.id = id; this.title = title; this.departmentId = departmentId;
    }
    /** @return course identifier */
    public Long getId() { return id; }
    /** @return course title */
    public String getTitle() { return title; }
    /** @return owning department identifier */
    public Long getDepartmentId() { return departmentId; }
    /** Sets generated identifier. @param id identifier */
    public void setId(Long id) { this.id = id; }
}
