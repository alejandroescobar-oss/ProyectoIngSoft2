package com.example.questionbank.api;

import com.example.questionbank.api.requests.CreateNameRequest;
import com.example.questionbank.api.requests.CreateUserRequest;
import com.example.questionbank.application.CatalogService;
import com.example.questionbank.domain.model.Competency;
import com.example.questionbank.domain.model.Topic;
import com.example.questionbank.domain.model.User;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/** HTTP endpoints for user and academic catalog setup. */
@RestController
@RequestMapping("/api")
public class CatalogController {
    private final CatalogService catalog;
    /** Injects the catalog application service. */
    public CatalogController(CatalogService catalog) { this.catalog = catalog; }
    /** Registers a user. */
    @PostMapping("/users")
    public User createUser(@Valid @RequestBody CreateUserRequest request) {
        return catalog.createUser(request.fullName(), request.email(), request.role());
    }
    /** Registers a topic. */
    @PostMapping("/topics")
    public Topic createTopic(@Valid @RequestBody CreateNameRequest request) {
        return catalog.createTopic(request.name());
    }
    /** Registers a competency. */
    @PostMapping("/competencies")
    public Competency createCompetency(@Valid @RequestBody CreateNameRequest request) {
        return catalog.createCompetency(request.name());
    }
}
