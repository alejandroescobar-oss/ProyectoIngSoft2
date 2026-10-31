package com.example.questionbank.api;

import com.example.questionbank.api.requests.CreateQuizRequest;
import com.example.questionbank.application.QuizService;
import com.example.questionbank.domain.model.Quiz;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/** HTTP endpoints for authoring quizzes. */
@RestController
@RequestMapping("/api/quizzes")
public class QuizController {
    private final QuizService quizzes;
    /** Injects the quiz application service. */
    public QuizController(QuizService quizzes) { this.quizzes = quizzes; }
    /** Creates a quiz from published questions only. */
    @PostMapping
    public Quiz create(@Valid @RequestBody CreateQuizRequest request) {
        return quizzes.create(request.title(), request.teacherId(), request.questionIds());
    }
    /** Lists saved quizzes. */
    @GetMapping
    public List<Quiz> list() { return quizzes.list(); }
}
