package com.taller2.plugins;

import core.contracts.QuestionPlugin;
import core.model.Question;
import core.model.QuestionRequest;
import java.util.UUID;

public final class GeneradorPreguntasSeleccionMultiple implements QuestionPlugin {
    @Override public String getName() { return "multiple-choice"; }

    @Override
    public boolean supports(String type) {
        return "MULTIPLE_CHOICE".equalsIgnoreCase(type);
    }

    @Override
    public Question generate(QuestionRequest request) {
        return new Question(UUID.randomUUID().toString(), request.getTitle(),
                request.getContent(), request.getType());
    }
}
