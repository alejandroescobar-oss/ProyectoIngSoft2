package gestionbancodepreguntas.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Question {
    private final QuestionType type;
    private final String statement;
    private final DifficultyLevel difficulty;
    private final String category;
    private final String author;
    private final String justification;
    private final String source;
    private final List<Option> options;
    private final String correctAnswer;
    private final Boolean trueFalseAnswer;
    private final String expectedAnswer;

    public Question(
            QuestionType type,
            String statement,
            DifficultyLevel difficulty,
            String category,
            String author,
            String justification,
            String source,
            List<String> options,
            String correctAnswer,
            Boolean trueFalseAnswer,
            String expectedAnswer) {
        this.type = type;
        this.statement = statement;
        this.difficulty = difficulty;
        this.category = category;
        this.author = author;
        this.justification = justification;
        this.source = source;
        List<Option> optionItems = new ArrayList<Option>(options.size());
        for (String option : options) {
            optionItems.add(new Option(option));
        }
        this.options = Collections.unmodifiableList(optionItems);
        this.correctAnswer = correctAnswer;
        this.trueFalseAnswer = trueFalseAnswer;
        this.expectedAnswer = expectedAnswer;
    }

    public QuestionType getType() {
        return type;
    }

    public String getStatement() {
        return statement;
    }

    public DifficultyLevel getDifficulty() {
        return difficulty;
    }

    public String getCategory() {
        return category;
    }

    public String getAuthor() {
        return author;
    }

    public String getJustification() {
        return justification;
    }

    public String getSource() {
        return source;
    }

    public List<String> getOptions() {
        List<String> optionTexts = new ArrayList<String>(options.size());
        for (Option option : options) {
            optionTexts.add(option.getText());
        }
        return Collections.unmodifiableList(optionTexts);
    }

    public List<Option> getOptionItems() {
        return options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public Boolean getTrueFalseAnswer() {
        return trueFalseAnswer;
    }

    public String getExpectedAnswer() {
        return expectedAnswer;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Tipo: ").append(type)
                .append("\nEnunciado: ").append(statement)
                .append("\nDificultad: ").append(difficulty)
                .append("\nCategoría: ").append(category)
                .append("\nAutor: ").append(author);

        if (!options.isEmpty()) {
            result.append("\nOpciones: ").append(getOptions())
                    .append("\nRespuesta correcta: ").append(correctAnswer);
        }
        if (trueFalseAnswer != null) {
            result.append("\nRespuesta correcta: ")
                    .append(trueFalseAnswer ? "Verdadero" : "Falso");
        }
        if (expectedAnswer != null) {
            result.append("\nRespuesta esperada: ").append(expectedAnswer);
        }
        if (justification != null && !justification.trim().isEmpty()) {
            result.append("\nJustificación: ").append(justification);
        }
        if (source != null && !source.trim().isEmpty()) {
            result.append("\nFuente: ").append(source);
        }
        return result.toString();
    }
}
