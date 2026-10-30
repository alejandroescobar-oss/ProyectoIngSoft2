# JPA Clean Persistence Demo

A small, layered Spring Boot application for teaching persistence with JPA and H2. It demonstrates how to keep domain objects free from persistence annotations while mapping relational concepts in separate JPA records.

## Learning objectives

- Separate the domain model from persistence technology.
- Define repository abstractions in the domain layer and implement them with JPA in infrastructure.
- Map one-to-many and many-to-one associations.
- Map a many-to-many association.
- Map inheritance using JPA's `JOINED` strategy.
- Explore the running application through a small REST API and the H2 console.

## Technology

- Java 17
- Spring Boot 3.4.5
- Spring Web
- Spring Data JPA / Hibernate
- H2 in-memory database
- Maven

## Architecture

```text
api/
  SchoolController                 HTTP endpoints
application/
  SchoolService                    use cases; depends on repository interfaces
domain/
  Person, Student, Instructor      plain Java domain objects
  Department, Course
  repository/                      technology-independent contracts
infrastructure/jpa/
  *RepositoryJpa                   concrete implementations of domain contracts
  DomainMapper                     domain <-> JPA conversion
  record/                          JPA entities and relationship annotations
  repository/                      Spring Data JPA interfaces
```

This is a layered architecture with dependency inversion at the repository boundary. It is **not** presented as hexagonal architecture: there are no ports-and-adapters packages or inbound/outbound port model. The domain classes contain no JPA annotations or imports. JPA annotations live only on persistence records.

## Persistence mappings

| Concept | Mapping |
|---|---|
| One-to-many | `DepartmentRecord.courses` uses `@OneToMany(mappedBy = "department")` |
| Many-to-one | `CourseRecord.department` uses `@ManyToOne` and `@JoinColumn` |
| Many-to-many | `StudentRecord.courses` uses `@ManyToMany` and join table `student_courses` |
| Inheritance | `PersonRecord` + `StudentRecord` + `InstructorRecord` use `@Inheritance(strategy = JOINED)` |

The inheritance hierarchy is intentionally separate from the plain domain hierarchy. This allows the domain model to stay persistence-agnostic while JPA owns table and mapping decisions.

## Requirements

- JDK 17+
- Maven 3.8+

Check your environment:

```bash
java -version
mvn -version
```

## Run

From the project root:

```bash
mvn clean test
mvn spring-boot:run
```

The API starts at `http://localhost:8080`.

H2 console: `http://localhost:8080/h2-console`

Use these H2 console settings:
- JDBC URL: `jdbc:h2:mem:schooldb`
- User Name: `sa`
- Password: leave blank

The database is in-memory and is recreated when the application starts (`create-drop`).

## API examples

Create a department:

```bash
curl -X POST http://localhost:8080/api/departments \
  -H "Content-Type: application/json" \
  -d '{"name":"Computer Science"}'
```

Create a course using the returned department ID:

```bash
curl -X POST http://localhost:8080/api/courses \
  -H "Content-Type: application/json" \
  -d '{"title":"Persistence with JPA","departmentId":1}'
```

Create a student:

```bash
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{"fullName":"Alex Morgan","email":"alex@example.com","studentNumber":"ST-1001"}'
```

List resources:

```bash
curl http://localhost:8080/api/departments
curl http://localhost:8080/api/courses
curl http://localhost:8080/api/students
```

## Important teaching note about many-to-many

The many-to-many mapping is present in the JPA persistence model. The API exposes enrollment using `POST /api/students/{studentId}/courses/{courseId}`. The JPA repository implementation loads both records, updates the owning student-side collection, and saves it inside a transaction. The returned domain student contains the enrolled course IDs.

## Important teaching note about inheritance

`StudentRecord` and `InstructorRecord` inherit shared columns from `PersonRecord`. With `JOINED`, Hibernate creates a base `people` table and subtype tables (`students` and `instructors`). This example uses a domain `Person` hierarchy too, but those classes are plain Java objects without persistence annotations.

## Notes

- The repository interfaces in `domain.repository` are the abstractions consumed by the application service.
- `SpringStudentDataRepository`, `SpringCourseDataRepository`, and `SpringDepartmentDataRepository` are infrastructure details.
- `DomainMapper` is the explicit translation point between domain objects and JPA records.
- For production use, add validation, exception handling, DTOs, migrations, and integration tests.
