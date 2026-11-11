package gestionbancodepreguntas.builder;

import gestionbancodepreguntas.domain.Question;
import gestionbancodepreguntas.domain.QuestionType;
import java.util.Collections;

public final class OpenQuestionBuilder extends QuestionBuilder {
    private String expectedAnswer;

    public void buildExpectedAnswer(String expectedAnswer) {
        this.expectedAnswer = expectedAnswer;
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.OPEN;
    }

    @Override
    public void configureTypeSpecificFields(QuestionSpec spec) {
        if (spec == null || spec.getType() != getQuestionType()) {
            throw new IllegalArgumentException(
                    "La especificación no corresponde a un builder de pregunta abierta.");
        }
        buildExpectedAnswer(spec.getExpectedAnswer());
    }

    @Override
    protected void resetTypeSpecificFields() {
        expectedAnswer = null;
    }

    @Override
    protected void validateTypeSpecificFields() {
        requireText(expectedAnswer, "La respuesta esperada de la pregunta abierta es obligatoria.");
    }

    @Override
    protected Question createQuestion() {
        return new Question(getQuestionType(), getStatement(), getDifficulty(), getCategory(),
                getAuthor(), getJustification(), getSource(), Collections.<String>emptyList(),
                null, null, expectedAnswer);
    }
}
