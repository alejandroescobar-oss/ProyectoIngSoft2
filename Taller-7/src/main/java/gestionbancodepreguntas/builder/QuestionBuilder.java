package gestionbancodepreguntas.builder;

import gestionbancodepreguntas.domain.DifficultyLevel;
import gestionbancodepreguntas.domain.Question;
import gestionbancodepreguntas.domain.QuestionType;

public abstract class QuestionBuilder {
    private String statement;
    private DifficultyLevel difficulty;
    private String category;
    private String author;
    private String justification;
    private String source;

    public final void reset() {
        statement = null;
        difficulty = null;
        category = null;
        author = null;
        justification = null;
        source = null;
        resetTypeSpecificFields();
    }

    public final void buildStatement(String statement) {
        this.statement = statement;
    }

    public final void buildDifficulty(DifficultyLevel difficulty) {
        this.difficulty = difficulty;
    }

    public final void buildCategory(String category) {
        this.category = category;
    }

    public final void buildAuthor(String author) {
        this.author = author;
    }

    public final void buildJustification(String justification) {
        this.justification = justification;
    }

    public final void buildSource(String source) {
        this.source = source;
    }

    public final Question build() {
        requireText(statement, "El enunciado es obligatorio.");
        if (difficulty == null) {
            throw new IllegalStateException("La dificultad es obligatoria.");
        }
        requireText(category, "La categoría es obligatoria.");
        requireText(author, "El autor es obligatorio.");
        validateTypeSpecificFields();
        return createQuestion();
    }

    public abstract QuestionType getQuestionType();

    public abstract void configureTypeSpecificFields(QuestionSpec spec);

    protected abstract void resetTypeSpecificFields();

    protected abstract void validateTypeSpecificFields();

    protected abstract Question createQuestion();

    protected final String getStatement() {
        return statement;
    }

    protected final DifficultyLevel getDifficulty() {
        return difficulty;
    }

    protected final String getCategory() {
        return category;
    }

    protected final String getAuthor() {
        return author;
    }

    protected final String getJustification() {
        return justification;
    }

    protected final String getSource() {
        return source;
    }

    protected static void requireText(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException(message);
        }
    }
}
