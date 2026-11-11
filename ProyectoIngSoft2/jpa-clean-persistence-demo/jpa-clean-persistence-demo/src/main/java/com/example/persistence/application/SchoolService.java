package com.example.persistence.application;

import com.example.persistence.domain.*;
import com.example.persistence.domain.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/** Application-layer use cases. Depends on repository abstractions, not JPA. */
@Service
@Transactional
public class SchoolService {
    private final StudentRepository students;
    private final CourseRepository courses;
    private final DepartmentRepository departments;

    /** Injects abstract repository contracts. */
    public SchoolService(StudentRepository students, CourseRepository courses, DepartmentRepository departments) {
        this.students = students; this.courses = courses; this.departments = departments;
    }
    /** Creates a department. */
    public Department createDepartment(String name) { return departments.save(new Department(null, name)); }
    /** Creates a course in an existing department. */
    public Course createCourse(String title, Long departmentId) { return courses.save(new Course(null, title, departmentId)); }
    /** Creates a student. */
    public Student createStudent(String name, String email, String studentNumber) {
        return students.save(new Student(null, name, email, studentNumber));
    }
    /** Enrolls a student in a course. */
    public Student enrollStudent(Long studentId, Long courseId) { return students.enrollInCourse(studentId, courseId); }
    /** Returns all departments. */
    @Transactional(readOnly = true) public List<Department> departments() { return departments.findAll(); }
    /** Returns all courses. */
    @Transactional(readOnly = true) public List<Course> courses() { return courses.findAll(); }
    /** Returns all students. */
    @Transactional(readOnly = true) public List<Student> students() { return students.findAll(); }
}
