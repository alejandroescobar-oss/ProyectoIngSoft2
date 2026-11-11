# Saber Pro Question Bank — JPA

Independent Spring Boot implementation for Taller 10. The original Swing/SQLite application and the school JPA demo remain separate.

## Requirements and execution

- JDK 17+
- Maven 3.8+

From PowerShell:

```powershell
Set-Location .\question-bank-jpa
mvn test
mvn spring-boot:run
```

The API listens on `http://localhost:8080`. H2 console: `http://localhost:8080/h2-console`; JDBC URL `jdbc:h2:mem:questionbank`, username `sa`, empty password. The in-memory schema is recreated on each start.

## Architecture

```text
api/                         REST controllers and request/response models
application/                 Catalog, question, review and quiz use cases
domain/model/                Persistence-independent business objects
domain/repository/           Technology-independent repository contracts
infrastructure/jpa/          JPA implementations and model mapper
infrastructure/jpa/record/   JPA entities, constraints and associations
infrastructure/jpa/repository/ Spring Data interfaces
```

Application services depend on domain repository contracts, never Spring Data interfaces. Domain classes do not import `jakarta.persistence`; `QuestionMapper` converts between domain questions and the separate `JOINED` JPA hierarchy. This is a layered application, not a ports-and-adapters implementation.

The four required association types are demonstrated: one-to-one question metadata (`question_id` unique in `question_metadata`), one-to-many answer options (FK in `answer_options`), many-to-one question author (FK in `questions`), and many-to-many question competencies (`question_competencies`). The question hierarchy uses `JOINED`: common fields in `questions`, subtype data in `multiple_choice_questions` and `true_false_questions`. Questions require a `User` with role `AUTHOR`, a topic and at least one competency. A multiple-choice draft can be edited while incomplete, but must have exactly five options and one correct answer to enter review. Only `PUBLISHED` questions can enter a quiz. A rejection returns a question to `DRAFT` and is stored in `question_reviews`.

## API walkthrough (PowerShell)

Create the catalog records and users; each response contains an `id`:

```powershell
$base = 'http://localhost:8080/api'
$author = Invoke-RestMethod -Method Post -Uri "$base/users" -ContentType 'application/json' -Body (@{fullName='Question Author'; email='author@example.com'; role='AUTHOR'} | ConvertTo-Json)
$reviewer = Invoke-RestMethod -Method Post -Uri "$base/users" -ContentType 'application/json' -Body (@{fullName='Question Reviewer'; email='reviewer@example.com'; role='REVIEWER'} | ConvertTo-Json)
$teacher = Invoke-RestMethod -Method Post -Uri "$base/users" -ContentType 'application/json' -Body (@{fullName='Course Teacher'; email='teacher@example.com'; role='TEACHER'} | ConvertTo-Json)
$topic = Invoke-RestMethod -Method Post -Uri "$base/topics" -ContentType 'application/json' -Body (@{name='Software Architecture'} | ConvertTo-Json)
$competency = Invoke-RestMethod -Method Post -Uri "$base/competencies" -ContentType 'application/json' -Body (@{name='Solution Design'} | ConvertTo-Json)
```

Create a multiple-choice question and submit it for review:

```powershell
$questionBody = @{
  statement = 'Which choice best describes separation of concerns?'
  difficulty = 'MEDIUM'
  authorId = $author.id
  topicId = $topic.id
  competencyIds = @($competency.id)
  metadata = @{ cognitiveLevel = 'ANALYSIS'; estimatedMinutes = 3; editorialNotes = 'Initial draft' }
  options = @(
    @{ text = 'Separate responsibilities into focused modules'; correct = $true }
    @{ text = 'Put all behavior in one class'; correct = $false }
    @{ text = 'Share mutable state across every module'; correct = $false }
    @{ text = 'Duplicate the same rules in each component'; correct = $false }
    @{ text = 'Remove all boundaries between components'; correct = $false }
  )
}
$question = Invoke-RestMethod -Method Post -Uri "$base/questions/multiple-choice" -ContentType 'application/json' -Body ($questionBody | ConvertTo-Json -Depth 5)
Invoke-RestMethod -Method Post -Uri "$base/questions/$($question.id)/submit"
Invoke-RestMethod -Method Post -Uri "$base/questions/$($question.id)/start-review?reviewerId=$($reviewer.id)"
Invoke-RestMethod -Method Post -Uri "$base/questions/$($question.id)/review" -ContentType 'application/json' -Body (@{reviewerId=$reviewer.id; decision='APPROVED'; comments='Approved'} | ConvertTo-Json)
Invoke-RestMethod -Method Post -Uri "$base/questions/$($question.id)/publish"
```

Create a quiz with the now-published question:

```powershell
Invoke-RestMethod -Method Post -Uri "$base/quizzes" -ContentType 'application/json' -Body (@{title='Architecture practice'; teacherId=$teacher.id; questionIds=@($question.id)} | ConvertTo-Json)
```

Other operations: `POST /api/questions/true-false`, `GET /api/questions`, `GET /api/questions/{id}`, `GET /api/questions?authorId={id}`, `GET /api/questions?status=PUBLISHED`, `GET /api/questions/published?competencyId={id}`, and `POST /api/questions/{id}/archive`. Invalid input returns 400, missing resources 404, and invalid state transitions 409.

## Verification

Run `mvn test`. `QuestionBankPersistenceTest` starts Spring with H2 and exercises `JOINED` subtype recovery, metadata/options/competency associations, review persistence, valid lifecycle transitions, incomplete-option rejection, and preventing drafts from entering quizzes.
