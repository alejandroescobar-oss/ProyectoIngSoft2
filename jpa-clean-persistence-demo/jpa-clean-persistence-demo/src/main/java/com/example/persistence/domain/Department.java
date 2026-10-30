package com.example.persistence.domain;

/** Academic department domain object. */
public class Department {
    private Long id;
    private String name;
    public Department() {}
    public Department(Long id, String name) { this.id = id; this.name = name; }
    /** @return department identifier */
    public Long getId() { return id; }
    /** @return department name */
    public String getName() { return name; }
    /** Sets generated identifier. @param id identifier */
    public void setId(Long id) { this.id = id; }
}
