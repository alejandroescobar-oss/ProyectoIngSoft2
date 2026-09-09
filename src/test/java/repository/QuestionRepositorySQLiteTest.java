package repository;

import model.Question;
import model.QuestionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuestionRepositorySQLiteTest {
    private static final String URL = "jdbc:sqlite:taller2-test.db";
    private QuestionRepositorySQLite repository;

    @BeforeEach
    void prepararBaseDeDatos() throws Exception {
        repository = new QuestionRepositorySQLite(URL);
        try (Connection connection = DriverManager.getConnection(URL);
             Statement statement = connection.createStatement()) {
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS preguntas (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        nombre TEXT NOT NULL,
                        texto TEXT NOT NULL,
                        opciones TEXT NOT NULL,
                        respuesta_correcta INTEGER NOT NULL,
                        estado TEXT NOT NULL
                    )
                    """);
            statement.executeUpdate("DELETE FROM preguntas");
        }
    }

    @Test
    void debeGuardarYLeerPreguntaConOpciones() {
        repository.guardar(new Question("Saber Pro 1", "¿Cuál es correcta?",
                List.of("A", "B", "C"), 1, QuestionStatus.BORRADOR));

        Question question = repository.listarTodas().get(0);
        assertEquals("Saber Pro 1", question.getNombre());
        assertEquals(List.of("A", "B", "C"), question.getOpciones());
        assertEquals(QuestionStatus.BORRADOR, question.getEstado());
    }

    @Test
    void debeActualizarEstadoYFiltrar() {
        repository.guardar(new Question("Pregunta", "Texto",
                List.of("Sí", "No"), 0, QuestionStatus.BORRADOR));
        Question question = repository.listarTodas().get(0);
        question.setEstado(QuestionStatus.PENDIENTE_REVISION);
        repository.actualizar(question);

        assertTrue(repository.listarPorEstado(QuestionStatus.BORRADOR).isEmpty());
        assertEquals(1, repository.listarPorEstado(QuestionStatus.PENDIENTE_REVISION).size());
    }
}
