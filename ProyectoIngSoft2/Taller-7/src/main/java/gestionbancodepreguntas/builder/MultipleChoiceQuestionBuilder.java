package gestionbancodepreguntas.builder;
import gestionbancodepreguntas.domain.Question;
import gestionbancodepreguntas.domain.QuestionType;
import java.util.ArrayList;
import java.util.List;

public final class MultipleChoiceQuestionBuilder extends QuestionBuilder {
    private final List<String> options = new ArrayList<String>();
    private String correctAnswer;

    public void buildOption(String option) {
        options.add(option);
    }

    public void buildCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.MULTIPLE_CHOICE;
    }

    @Override
    public void configureTypeSpecificFields(QuestionSpec spec) {
        requireMatchingType(spec);
        for (String option : spec.getOptions()) {
            buildOption(option);
        }
        buildCorrectAnswer(spec.getCorrectAnswer());
    }

    @Override
    protected void resetTypeSpecificFields() {
        options.clear();
        correctAnswer = null;
    }

    @Override
    protected void validateTypeSpecificFields() {
        if (options.size() < 2) {
            throw new IllegalStateException(
                    "Una pregunta de selección múltiple requiere al menos dos opciones.");
        }
        for (String option : options) {
            requireText(option, "Las opciones no pueden estar vacías.");
        }
        requireText(correctAnswer, "La respuesta correcta es obligatoria.");
        if (!options.contains(correctAnswer)) {
            throw new IllegalStateException(
                    "La respuesta correcta debe coincidir con una de las opciones.");
        }
    }

    @Override
    protected Question createQuestion() {
        return new Question(getQuestionType(), getStatement(), getDifficulty(), getCategory(),
                getAuthor(), getJustification(), getSource(), options, correctAnswer, null, null);
    }

    private void requireMatchingType(QuestionSpec spec) {
        if (spec == null || spec.getType() != getQuestionType()) {
            throw new IllegalArgumentException(
                    "La especificación no corresponde a un builder de selección múltiple.");
        }
    }
}
