package gestionbancodepreguntas.test;

import gestionbancodepreguntas.builder.QuestionSpec;
import gestionbancodepreguntas.builder.TrueFalseQuestionBuilder;
import gestionbancodepreguntas.director.QuestionDirector;
import gestionbancodepreguntas.domain.DifficultyLevel;
import gestionbancodepreguntas.domain.Question;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class TrueFalseQuestionBuilderTest {
    @Test
    void buildsTrueAnswer() {
        Question question = buildQuestion(true);
        assertEquals(Boolean.TRUE, question.getTrueFalseAnswer());
    }

    @Test
    void buildsFalseAnswer() {
        Question question = buildQuestion(false);
        assertEquals(Boolean.FALSE, question.getTrueFalseAnswer());
    }

    private static Question buildQuestion(boolean answer) {
        return new QuestionDirector().constructQuestion(new TrueFalseQuestionBuilder(),
                QuestionSpec.trueFalse("La Tierra gira alrededor del Sol.",
                        DifficultyLevel.EASY, "Ciencias", "Ana", null, null, answer));
    }
}
