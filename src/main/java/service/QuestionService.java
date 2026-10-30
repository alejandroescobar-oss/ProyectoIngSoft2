package service;

import model.Question;
import model.QuestionStatus;
import repository.QuestionRepository;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class QuestionService {
    public static final String STATUS_PROPERTY = "estado";

    private static final Map<QuestionStatus, List<QuestionStatus>> ALLOWED_TRANSITIONS =
            createAllowedTransitions();

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
    /**
     * Archiva una pregunta conservando su registro y notifica a los observadores.
     *
     * @param id identificador de la pregunta que se va a archivar
     */
    public void archivar(int id) {
        Question question = buscarPorId(id);
        if (question != null) {
            cambiarEstado(question, QuestionStatus.ARCHIVADA);
        }
    }
    public void actualizar(Question question) { repository.actualizar(question); }

    /**
     * Aplica una transición válida del ciclo de vida y notifica el cambio
     * a los observadores registrados.
     *
     * @param question pregunta cuyo estado se va a cambiar
     * @param nuevoEstado estado de destino del ciclo de vida
     * @throws IllegalArgumentException si falta algún argumento o la transición
     *                                  solicitada no está permitida
     */
    public void cambiarEstado(Question question, QuestionStatus nuevoEstado) {
        if (question == null || nuevoEstado == null) {
            throw new IllegalArgumentException("La pregunta y el estado son obligatorios.");
        }
        QuestionStatus anterior = question.getEstado();
        if (anterior == nuevoEstado) {
            return;
        }
        if (anterior == null || !ALLOWED_TRANSITIONS.get(anterior).contains(nuevoEstado)) {
            throw new IllegalArgumentException(
                    "Transición de estado no permitida: "
                            + (anterior == null ? "sin estado" : anterior.getDisplayName())
                            + " → " + nuevoEstado.getDisplayName());
        }
        question.setEstado(nuevoEstado);
        try {
            if (nuevoEstado == QuestionStatus.ARCHIVADA) {
                repository.archivar(question.getId());
            } else {
                repository.actualizar(question);
            }
        } catch (RuntimeException exception) {
            question.setEstado(anterior);
            throw exception;
        }
        support.firePropertyChange(STATUS_PROPERTY, anterior, nuevoEstado);
    }

    /**
     * Define las transiciones del ciclo de vida permitidas para las preguntas.
     *
     * @return mapa inmutable entre cada estado y sus estados siguientes permitidos
     */
    private static Map<QuestionStatus, List<QuestionStatus>> createAllowedTransitions() {
        Map<QuestionStatus, List<QuestionStatus>> transitions = new EnumMap<>(QuestionStatus.class);
        transitions.put(QuestionStatus.BORRADOR,
                List.of(QuestionStatus.PENDIENTE_REVISION, QuestionStatus.ARCHIVADA));
        transitions.put(QuestionStatus.PENDIENTE_REVISION,
                List.of(QuestionStatus.EN_REVISION, QuestionStatus.ARCHIVADA));
        transitions.put(QuestionStatus.EN_REVISION,
                List.of(QuestionStatus.APROBADA, QuestionStatus.RECHAZADA, QuestionStatus.ARCHIVADA));
        transitions.put(QuestionStatus.APROBADA,
                List.of(QuestionStatus.PUBLICADA, QuestionStatus.ARCHIVADA));
        transitions.put(QuestionStatus.RECHAZADA,
                List.of(QuestionStatus.BORRADOR, QuestionStatus.ARCHIVADA));
        transitions.put(QuestionStatus.PUBLICADA, List.of(QuestionStatus.ARCHIVADA));
        transitions.put(QuestionStatus.ARCHIVADA, List.of());
        return Map.copyOf(transitions);
    }
}
