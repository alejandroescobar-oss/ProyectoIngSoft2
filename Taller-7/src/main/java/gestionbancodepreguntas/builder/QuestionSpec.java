package gestionbancodepreguntas.builder;

import gestionbancodepreguntas.domain.DifficultyLevel;
import gestionbancodepreguntas.domain.QuestionType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class QuestionSpec {
    private final QuestionType type;
    private final String statement;
    private final DifficultyLevel difficulty;
    private final String category;
    private final String author;
    private final String justification;
    private final String source;
    private final List<String> options;
    private final String correctAnswer;
    private final Boolean trueFalseAnswer;
    private final String expectedAnswer;

    private QuestionSpec(
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
        this.options = Collections.unmodifiableList(new ArrayList<String>(options));
        this.correctAnswer = correctAnswer;
        this.trueFalseAnswer = trueFalseAnswer;
        this.expectedAnswer = expectedAnswer;
    }

    public static QuestionSpec multipleChoice(
            String statement, DifficultyLevel difficulty, String category, String author,
            String justification, String source, List<String> options, String correctAnswer) {
        return new QuestionSpec(QuestionType.MULTIPLE_CHOICE, statement, difficulty, category,
                author, justification, source, options, correctAnswer, null, null);
    }

    public static QuestionSpec trueFalse(
            String statement, DifficultyLevel difficulty, String category, String author,
            String justification, String source, boolean correctAnswer) {
        return new QuestionSpec(QuestionType.TRUE_FALSE, statement, difficulty, category,
                author, justification, source, Collections.<String>emptyList(), null,
                Boolean.valueOf(correctAnswer), null);
    }

    public static QuestionSpec open(
            String statement, DifficultyLevel difficulty, String category, String author,
            String justification, String source, String expectedAnswer) {
        return new QuestionSpec(QuestionType.OPEN, statement, difficulty, category, author,
                justification, source, Collections.<String>emptyList(), null, null,
                expectedAnswer);
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
}
