package com.taller2.plugins;

import core.contracts.QuestionPlugin;
import core.model.Question;
import core.model.QuestionRequest;
import java.util.UUID;

public final class GeneradorPreguntaMultimedia implements QuestionPlugin {
    @Override public String getName() { return "multimedia"; }

    @Override public boolean supports(String type) {
        return "MULTIMEDIA".equalsIgnoreCase(type);
    }

    @Override
    public Question generate(QuestionRequest request) {
        return new Question(UUID.randomUUID().toString(), request.getTitle(),
                request.getContent(), request.getType());
    }
}
