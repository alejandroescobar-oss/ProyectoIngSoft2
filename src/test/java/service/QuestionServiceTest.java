package service;

import model.Question;
import model.QuestionStatus;
import org.junit.jupiter.api.Test;
import repository.QuestionRepository;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QuestionServiceTest {
    @Test
    void debeNotificarCuandoCambiaEstado() {
        MemoryRepository repository = new MemoryRepository();
        Question question = new Question(1, "Pregunta", "Texto",
                List.of("A", "B"), 0, QuestionStatus.BORRADOR);
        repository.questions.add(question);
        QuestionService service = new QuestionService(repository);
        List<PropertyChangeEvent> events = new ArrayList<>();
        service.addPropertyChangeListener(events::add);

        service.cambiarEstado(question, QuestionStatus.PENDIENTE_REVISION);

        assertEquals(1, events.size());
        assertEquals("estado", events.get(0).getPropertyName());
        assertEquals(QuestionStatus.BORRADOR, events.get(0).getOldValue());
        assertEquals(QuestionStatus.PENDIENTE_REVISION, events.get(0).getNewValue());
    }

    private static class MemoryRepository implements QuestionRepository {
        private final List<Question> questions = new ArrayList<>();

        public void guardar(Question question) { questions.add(question); }
        public Optional<Question> buscarPorId(int id) {
            return questions.stream().filter(q -> q.getId() == id).findFirst();
        }
        public List<Question> listarTodas() { return questions; }
        public void actualizar(Question question) { }
        public void eliminar(int id) { questions.removeIf(q -> q.getId() == id); }
        public List<Question> listarPorEstado(QuestionStatus status) {
            return questions.stream().filter(q -> q.getEstado() == status).toList();
        }
    }
}
