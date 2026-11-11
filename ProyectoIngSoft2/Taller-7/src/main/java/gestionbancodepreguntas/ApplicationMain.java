package gestionbancodepreguntas;

import gestionbancodepreguntas.builder.MultipleChoiceQuestionBuilder;
import gestionbancodepreguntas.builder.OpenQuestionBuilder;
import gestionbancodepreguntas.builder.QuestionSpec;
import gestionbancodepreguntas.builder.TrueFalseQuestionBuilder;
import gestionbancodepreguntas.director.QuestionDirector;
import gestionbancodepreguntas.domain.DifficultyLevel;
import gestionbancodepreguntas.domain.Question;
import java.util.Arrays;

public final class ApplicationMain {
    private ApplicationMain() {
    }

    public static void main(String[] args) {
        QuestionDirector director = new QuestionDirector();

        Question multipleChoice = director.constructQuestion(
                new MultipleChoiceQuestionBuilder(),
                QuestionSpec.multipleChoice(
                        "¿Cuál es la capital de Colombia?",
                        DifficultyLevel.EASY,
                        "Geografía",
                        "Ana Pérez",
                        "Bogotá es la capital del país.",
                        "Atlas de Colombia",
                        Arrays.asList("Bogotá", "Cali", "Medellín"),
                        "Bogotá"));

        Question trueFalse = director.constructQuestion(
                new TrueFalseQuestionBuilder(),
                QuestionSpec.trueFalse(
                        "La Tierra gira alrededor del Sol.",
                        DifficultyLevel.EASY,
                        "Ciencias",
                        "Luis Gómez",
                        "La Tierra completa una órbita alrededor del Sol.",
                        "Texto de ciencias naturales",
                        true));

        Question open = director.constructQuestion(
                new OpenQuestionBuilder(),
                QuestionSpec.open(
                        "Explique por qué es importante reciclar.",
                        DifficultyLevel.MEDIUM,
                        "Medio ambiente",
                        "María Ruiz",
                        "Se espera que mencione la reducción de residuos y el ahorro de recursos.",
                        null,
                        "Reciclar reduce los residuos y permite reutilizar materiales."));

        System.out.println("=== Pregunta de selección múltiple ===");
        System.out.println(multipleChoice);
        System.out.println("\n=== Pregunta de verdadero/falso ===");
        System.out.println(trueFalse);
        System.out.println("\n=== Pregunta abierta ===");
        System.out.println(open);
    }
}
