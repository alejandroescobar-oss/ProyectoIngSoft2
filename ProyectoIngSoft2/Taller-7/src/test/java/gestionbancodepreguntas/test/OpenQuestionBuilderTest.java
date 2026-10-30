package gestionbancodepreguntas.test;

import gestionbancodepreguntas.builder.OpenQuestionBuilder;
import gestionbancodepreguntas.builder.QuestionSpec;
import gestionbancodepreguntas.director.QuestionDirector;
import gestionbancodepreguntas.domain.DifficultyLevel;
import gestionbancodepreguntas.domain.Question;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class OpenQuestionBuilderTest {
    @Test
    void buildsOpenQuestion() {
        Question question = new QuestionDirector().constructQuestion(new OpenQuestionBuilder(),
                QuestionSpec.open("Defina evaporación.", DifficultyLevel.MEDIUM, "Ciencias",
                        "Ana", null, null, "Paso de líquido a gas."));
        assertEquals("Paso de líquido a gas.", question.getExpectedAnswer());
        assertEquals(0, question.getOptions().size());
    }

    @Test
    void rejectsMissingExpectedAnswer() {
        assertThrows(IllegalStateException.class, () ->
                new QuestionDirector().constructQuestion(new OpenQuestionBuilder(),
                        QuestionSpec.open("Defina evaporación.", DifficultyLevel.MEDIUM, "Ciencias",
                                "Ana", null, null, " ")));
    }
}
