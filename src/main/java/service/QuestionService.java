package service;

import model.Question;
import model.QuestionStatus;
import repository.QuestionRepository;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

public class QuestionService {
    public static final String STATUS_PROPERTY = "estado";

    private final QuestionRepository repository;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public QuestionService(QuestionRepository repository) {
        this.repository = repository;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public List<Question> listarTodas() { return repository.listarTodas(); }
    public List<Question> listarPorEstado(QuestionStatus estado) {
        return repository.listarPorEstado(estado);
    }
    public Question buscarPorId(int id) {
        return repository.buscarPorId(id).orElse(null);
    }
    public void guardar(Question question) { repository.guardar(question); }
    public void eliminar(int id) { repository.eliminar(id); }
    public void actualizar(Question question) { repository.actualizar(question); }

    public void cambiarEstado(Question question, QuestionStatus nuevoEstado) {
        if (question == null || nuevoEstado == null) {
            throw new IllegalArgumentException("La pregunta y el estado son obligatorios.");
        }
        QuestionStatus anterior = question.getEstado();
        if (anterior == nuevoEstado) {
            return;
        }
        question.setEstado(nuevoEstado);
        repository.actualizar(question);
        support.firePropertyChange(STATUS_PROPERTY, anterior, nuevoEstado);
    }
}
