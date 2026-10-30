package gestionbancodepreguntas.director;

import gestionbancodepreguntas.builder.QuestionBuilder;
import gestionbancodepreguntas.builder.QuestionSpec;
import gestionbancodepreguntas.domain.Question;

public final class QuestionDirector {
    public Question constructQuestion(QuestionBuilder builder, QuestionSpec spec) {
        if (builder == null) {
            throw new IllegalArgumentException("El builder no puede ser nulo.");
        }
        if (spec == null) {
            throw new IllegalArgumentException("La especificación no puede ser nula.");
        }

        builder.reset();
        builder.buildStatement(spec.getStatement());
        builder.buildDifficulty(spec.getDifficulty());
        builder.buildCategory(spec.getCategory());
        builder.buildAuthor(spec.getAuthor());
        builder.buildJustification(spec.getJustification());
        builder.buildSource(spec.getSource());
        builder.configureTypeSpecificFields(spec);
        return builder.build();
    }
}
