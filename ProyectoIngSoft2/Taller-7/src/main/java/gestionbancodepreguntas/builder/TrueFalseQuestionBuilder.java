package gestionbancodepreguntas.builder;

import gestionbancodepreguntas.domain.Question;
import gestionbancodepreguntas.domain.QuestionType;
import java.util.Collections;

public final class TrueFalseQuestionBuilder extends QuestionBuilder {
    private Boolean correctAnswer;

    public void buildCorrectAnswer(boolean correctAnswer) {
        this.correctAnswer = Boolean.valueOf(correctAnswer);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.TRUE_FALSE;
    }

    @Override
    public void configureTypeSpecificFields(QuestionSpec spec) {
        if (spec == null || spec.getType() != getQuestionType()) {
            throw new IllegalArgumentException(
                    "La especificación no corresponde a un builder de verdadero/falso.");
        }
        if (spec.getTrueFalseAnswer() == null) {
            throw new IllegalArgumentException("La respuesta de verdadero/falso es obligatoria.");
        }
        buildCorrectAnswer(spec.getTrueFalseAnswer().booleanValue());
    }

    @Override
    protected void resetTypeSpecificFields() {
        correctAnswer = null;
    }

    @Override
    protected void validateTypeSpecificFields() {
        if (correctAnswer == null) {
            throw new IllegalStateException(
                    "La respuesta correcta de verdadero/falso es obligatoria.");
        }
    }

    @Override
    protected Question createQuestion() {
        return new Question(getQuestionType(), getStatement(), getDifficulty(), getCategory(),
                getAuthor(), getJustification(), getSource(), Collections.<String>emptyList(),
                null, correctAnswer, null);
    }
}
