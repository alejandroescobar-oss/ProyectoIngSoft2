package com.example.persistence.api;

import com.example.persistence.application.SchoolService;
import com.example.persistence.domain.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/** REST endpoints for exploring the persistence example. */
@RestController
@RequestMapping("/api")
public class SchoolController {
    private final SchoolService service;
    /** Creates the controller. @param service application use cases */
    public SchoolController(SchoolService service) { this.service = service; }

    /** Creates a department. */
    @PostMapping("/departments") public Department createDepartment(@RequestBody NameRequest request) {
        return service.createDepartment(request.name());
    }
    /** Creates a course. */
    @PostMapping("/courses") public Course createCourse(@RequestBody CourseRequest request) {
        return service.createCourse(request.title(), request.departmentId());
    }
    /** Creates a student. */
    @PostMapping("/students") public Student createStudent(@RequestBody StudentRequest request) {
        return service.createStudent(request.fullName(), request.email(), request.studentNumber());
    }
    /** Enrolls a student in a course, demonstrating the many-to-many relationship. */
    @PostMapping("/students/{studentId}/courses/{courseId}")
    public Student enrollStudent(@PathVariable Long studentId, @PathVariable Long courseId) {
        return service.enrollStudent(studentId, courseId);
    }
    /** Lists departments. */
    @GetMapping("/departments") public List<Department> departments() { return service.departments(); }
    /** Lists courses. */
    @GetMapping("/courses") public List<Course> courses() { return service.courses(); }
    /** Lists students. */
    @GetMapping("/students") public List<Student> students() { return service.students(); }

    /** Request body for named resources. */
    public record NameRequest(String name) {}
    /** Request body for courses. */
    public record CourseRequest(String title, Long departmentId) {}
    /** Request body for students. */
    public record StudentRequest(String fullName, String email, String studentNumber) {}
}
