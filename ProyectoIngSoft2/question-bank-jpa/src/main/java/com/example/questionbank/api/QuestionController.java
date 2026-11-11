package com.example.questionbank.api;

import com.example.questionbank.api.requests.*;
import com.example.questionbank.application.QuestionService;
import com.example.questionbank.application.ReviewService;
import com.example.questionbank.domain.model.AnswerOption;
import com.example.questionbank.domain.model.Question;
import com.example.questionbank.domain.model.QuestionMetadata;
import com.example.questionbank.domain.model.QuestionStatus;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/** HTTP endpoints for question authoring and lifecycle transitions. */
@RestController
@RequestMapping("/api/questions")
public class QuestionController {
    private final QuestionService questions;
    private final ReviewService reviews;
    /** Injects application services; controllers do not access persistence directly. */
    public QuestionController(QuestionService questions, ReviewService reviews) {
        this.questions = questions;
        this.reviews = reviews;
    }
    /** Creates a multiple-choice question. */
    @PostMapping("/multiple-choice")
    public QuestionResponse createMultipleChoice(@Valid @RequestBody CreateMultipleChoiceQuestionRequest request) {
        List<AnswerOption> options = request.options().stream().map(AnswerOptionRequest::toDomain).toList();
        QuestionMetadata metadata = request.metadata() == null ? null : request.metadata().toDomain();
        return QuestionResponse.from(questions.createMultipleChoice(request.statement(), request.difficulty(),
                request.authorId(), request.topicId(), request.competencyIds(), metadata, options));
    }
    /** Creates a true/false question. */
    @PostMapping("/true-false")
    public QuestionResponse createTrueFalse(@Valid @RequestBody CreateTrueFalseQuestionRequest request) {
        QuestionMetadata metadata = request.metadata() == null ? null : request.metadata().toDomain();
        return QuestionResponse.from(questions.createTrueFalse(request.statement(), request.difficulty(),
                request.authorId(), request.topicId(), request.competencyIds(), metadata, request.expectedAnswer()));
    }
    /** Returns a question by identifier. */
    @GetMapping("/{id}")
    public QuestionResponse get(@PathVariable Long id) { return QuestionResponse.from(questions.get(id)); }
    /** Lists questions with optional author or status filters. */
    @GetMapping
    public List<QuestionResponse> list(@RequestParam(required = false) Long authorId,
                                       @RequestParam(required = false) QuestionStatus status) {
        return questions.list(authorId, status).stream().map(QuestionResponse::from).toList();
    }
    /** Lists published questions associated with a competency. */
    @GetMapping("/published")
    public List<QuestionResponse> publishedByCompetency(@RequestParam Long competencyId) {
        return questions.listPublishedByCompetency(competencyId).stream().map(QuestionResponse::from).toList();
    }
    /** Submits a valid draft for review. */
    @PostMapping("/{id}/submit")
    public QuestionResponse submit(@PathVariable Long id) { return QuestionResponse.from(questions.submitForReview(id)); }
    /** Assigns a pending question to a reviewer. */
    @PostMapping("/{id}/start-review")
    public QuestionResponse startReview(@PathVariable Long id, @RequestParam Long reviewerId) {
        return QuestionResponse.from(questions.startReview(id, reviewerId));
    }
    /** Records approval or rejection by a reviewer. */
    @PostMapping("/{id}/review")
    public QuestionResponse decide(@PathVariable Long id, @Valid @RequestBody ReviewDecisionRequest request) {
        return QuestionResponse.from(reviews.decide(id, request.reviewerId(), request.decision(), request.comments()));
    }
    /** Publishes an approved question. */
    @PostMapping("/{id}/publish")
    public QuestionResponse publish(@PathVariable Long id) { return QuestionResponse.from(questions.publish(id)); }
    /** Archives a published question. */
    @PostMapping("/{id}/archive")
    public QuestionResponse archive(@PathVariable Long id) { return QuestionResponse.from(questions.archive(id)); }
}
