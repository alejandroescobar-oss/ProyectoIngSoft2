package gestionbancodepreguntas.test;

import gestionbancodepreguntas.builder.MultipleChoiceQuestionBuilder;
import gestionbancodepreguntas.builder.OpenQuestionBuilder;
import gestionbancodepreguntas.builder.QuestionSpec;
import gestionbancodepreguntas.builder.TrueFalseQuestionBuilder;
import gestionbancodepreguntas.director.QuestionDirector;
import gestionbancodepreguntas.domain.DifficultyLevel;
import gestionbancodepreguntas.domain.Question;
import gestionbancodepreguntas.domain.QuestionType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class MultipleChoiceQuestionBuilderTest {
    @Test
    void buildsMultipleChoiceQuestion() {
        Question question = new QuestionDirector().constructQuestion(
                new MultipleChoiceQuestionBuilder(), multipleChoiceSpec("Bogotá"));
        assertEquals(QuestionType.MULTIPLE_CHOICE, question.getType());
        assertEquals("Bogotá", question.getCorrectAnswer());
        assertEquals(3, question.getOptions().size());
    }

    @Test
    void rejectsQuestionWithoutStatement() {
        expectThrows(IllegalStateException.class, new Action() {
            @Override
            public void run() {
                new QuestionDirector().constructQuestion(
                        new MultipleChoiceQuestionBuilder(),
                        QuestionSpec.multipleChoice(null, DifficultyLevel.EASY,
                                "Geografía", "Ana", null, null,
                                Arrays.asList("Bogotá", "Cali"), "Bogotá"));
            }
        });
    }

    @Test
    void rejectsMultipleChoiceWithoutOptions() {
        expectThrows(IllegalStateException.class, new Action() {
            @Override
            public void run() {
                new QuestionDirector().constructQuestion(
                        new MultipleChoiceQuestionBuilder(),
                        QuestionSpec.multipleChoice("Enunciado", DifficultyLevel.EASY,
                                "Categoría", "Autor", null, null,
                                Arrays.<String>asList(), "Bogotá"));
            }
        });
    }

    @Test
    void rejectsCorrectAnswerNotPresentInOptions() {
        expectThrows(IllegalStateException.class, new Action() {
            @Override
            public void run() {
                new QuestionDirector().constructQuestion(
                        new MultipleChoiceQuestionBuilder(), multipleChoiceSpec("Cartagena"));
            }
        });
    }

    @Test
    void buildsDifferentQuestionTypesThroughSameDirector() {
        QuestionDirector director = new QuestionDirector();
        Question trueFalse = director.constructQuestion(
                new TrueFalseQuestionBuilder(),
                QuestionSpec.trueFalse("El agua hierve a 100 °C.", DifficultyLevel.EASY,
                        "Ciencias", "Autor", null, null, true));
        Question open = director.constructQuestion(
                new OpenQuestionBuilder(),
                QuestionSpec.open("Defina evaporación.", DifficultyLevel.MEDIUM,
                        "Ciencias", "Autor", null, null, "Paso de líquido a gas."));

        assertEquals(QuestionType.TRUE_FALSE, trueFalse.getType());
        assertEquals(Boolean.TRUE, trueFalse.getTrueFalseAnswer());
        assertEquals(QuestionType.OPEN, open.getType());
        assertEquals("Paso de líquido a gas.", open.getExpectedAnswer());
    }

    @Test
    void rejectsOpenQuestionWithoutExpectedAnswer() {
        expectThrows(IllegalStateException.class, new Action() {
            @Override
            public void run() {
                new QuestionDirector().constructQuestion(
                        new OpenQuestionBuilder(),
                        QuestionSpec.open("Defina evaporación.", DifficultyLevel.MEDIUM,
                                "Ciencias", "Autor", null, null, " "));
            }
        });
    }

    @Test
    void preservesProductImmutability() {
        final Question question = new QuestionDirector().constructQuestion(
                new MultipleChoiceQuestionBuilder(), multipleChoiceSpec("Bogotá"));
        expectThrows(UnsupportedOperationException.class, new Action() {
            @Override
            public void run() {
                question.getOptions().add("Barranquilla");
            }
        });
    }

    @Test
    void rejectsNullBuilder() {
        expectThrows(IllegalArgumentException.class, new Action() {
            @Override
            public void run() {
                new QuestionDirector().constructQuestion(null, multipleChoiceSpec("Bogotá"));
            }
        });
    }

    @Test
    void rejectsNullSpecification() {
        expectThrows(IllegalArgumentException.class, new Action() {
            @Override
            public void run() {
                new QuestionDirector().constructQuestion(
                        new MultipleChoiceQuestionBuilder(), null);
            }
        });
    }

    @Test
    void rejectsQuestionWithoutDifficulty() {
        expectThrows(IllegalStateException.class, new Action() {
            @Override
            public void run() {
                new QuestionDirector().constructQuestion(new MultipleChoiceQuestionBuilder(),
                        QuestionSpec.multipleChoice("Enunciado", null, "Geografía", "Ana",
                                null, null, Arrays.asList("Bogotá", "Cali"), "Bogotá"));
            }
        });
    }

    @Test
    void rejectsQuestionWithoutCategory() {
        expectThrows(IllegalStateException.class, new Action() {
            @Override
            public void run() {
                new QuestionDirector().constructQuestion(new MultipleChoiceQuestionBuilder(),
                        QuestionSpec.multipleChoice("Enunciado", DifficultyLevel.EASY, " ",
                                "Ana", null, null, Arrays.asList("Bogotá", "Cali"), "Bogotá"));
            }
        });
    }

    @Test
    void rejectsQuestionWithoutAuthor() {
        expectThrows(IllegalStateException.class, new Action() {
            @Override
            public void run() {
                new QuestionDirector().constructQuestion(new MultipleChoiceQuestionBuilder(),
                        QuestionSpec.multipleChoice("Enunciado", DifficultyLevel.EASY,
                                "Geografía", null, null, null,
                                Arrays.asList("Bogotá", "Cali"), "Bogotá"));
            }
        });
    }

    @Test
    void rejectsBlankMultipleChoiceOption() {
        expectThrows(IllegalStateException.class, new Action() {
            @Override
            public void run() {
                new QuestionDirector().constructQuestion(new MultipleChoiceQuestionBuilder(),
                        QuestionSpec.multipleChoice("Enunciado", DifficultyLevel.EASY,
                                "Geografía", "Ana", null, null,
                                Arrays.asList("Bogotá", " "), "Bogotá"));
            }
        });
    }

    @Test
    void buildsFalseTrueFalseQuestion() {
        Question question = new QuestionDirector().constructQuestion(
                new TrueFalseQuestionBuilder(),
                QuestionSpec.trueFalse("La Tierra es plana.", DifficultyLevel.EASY,
                        "Ciencias", "Ana", null, null, false));
        assertEquals(Boolean.FALSE, question.getTrueFalseAnswer());
        assertEquals(0, question.getOptions().size());
    }

    @Test
    void preservesOptionalMetadata() {
        Question question = new QuestionDirector().constructQuestion(
                new OpenQuestionBuilder(),
                QuestionSpec.open("Defina evaporación.", DifficultyLevel.MEDIUM, "Ciencias",
                        "Ana", "Cambio de estado.", "Libro de ciencias", "Líquido a gas."));
        assertEquals("Cambio de estado.", question.getJustification());
        assertEquals("Libro de ciencias", question.getSource());
    }

    @Test
    void resetsBuilderBetweenQuestions() {
        MultipleChoiceQuestionBuilder builder = new MultipleChoiceQuestionBuilder();
        QuestionDirector director = new QuestionDirector();
        director.constructQuestion(builder, multipleChoiceSpec("Bogotá"));
        Question secondQuestion = director.constructQuestion(builder,
                QuestionSpec.multipleChoice("¿Capital de Perú?", DifficultyLevel.MEDIUM,
                        "Geografía", "Luis", null, null,
                        Arrays.asList("Lima", "Quito"), "Lima"));
        assertEquals("¿Capital de Perú?", secondQuestion.getStatement());
        assertEquals(Arrays.asList("Lima", "Quito"), secondQuestion.getOptions());
        assertEquals("Luis", secondQuestion.getAuthor());
    }

    @Test
    void rejectsMismatchedBuilderSpecification() {
        expectThrows(IllegalArgumentException.class, new Action() {
            @Override
            public void run() {
                new OpenQuestionBuilder().configureTypeSpecificFields(multipleChoiceSpec("Bogotá"));
            }
        });
    }

    @Test
    void preservesSpecificationOptionImmutability() {
        List<String> options = new ArrayList<String>(Arrays.asList("Bogotá", "Cali"));
        QuestionSpec spec = QuestionSpec.multipleChoice("Capital de Colombia?",
                DifficultyLevel.EASY, "Geografía", "Ana", null, null, options, "Bogotá");
        options.add("Medellín");
        assertEquals(Arrays.asList("Bogotá", "Cali"), spec.getOptions());
        expectThrows(UnsupportedOperationException.class, new Action() {
            @Override
            public void run() {
                spec.getOptions().add("Medellín");
            }
        });
    }

    @Test
    void formatsQuestionWithOptionalMetadata() {
        Question question = new QuestionDirector().constructQuestion(
                new OpenQuestionBuilder(),
                QuestionSpec.open("Defina evaporación.", DifficultyLevel.MEDIUM, "Ciencias",
                        "Ana", "Cambio de estado.", "Libro de ciencias", "Líquido a gas."));
        String text = question.toString();
        assertTrue(text.contains("Justificación: Cambio de estado."));
        assertTrue(text.contains("Fuente: Libro de ciencias"));
        assertTrue(text.contains("Respuesta esperada: Líquido a gas."));
    }

    @Test
    void storesOptionsAsDomainObjects() {
        Question question = new QuestionDirector().constructQuestion(
                new MultipleChoiceQuestionBuilder(), multipleChoiceSpec("Bogotá"));
        assertEquals("Bogotá", question.getOptionItems().get(0).getText());
    }

    private static QuestionSpec multipleChoiceSpec(String correctAnswer) {
        return QuestionSpec.multipleChoice("¿Cuál es la capital de Colombia?",
                DifficultyLevel.EASY, "Geografía", "Ana", null, null,
                Arrays.asList("Bogotá", "Cali", "Medellín"), correctAnswer);
    }

    private static void expectThrows(Class<? extends Throwable> expected, Action action) {
        assertThrows(expected, action::run);
    }

    private interface Action {
        void run();
    }
}
