package core;

import model.Question;
import model.QuestionStatus;
import repository.QuestionRepository;
import repository.QuestionRepositorySQLite;

import java.util.List;

public class BancoPreguntas {
    private final QuestionRepository repositorio;

    public BancoPreguntas() {
        this.repositorio = new QuestionRepositorySQLite();
    }

    public BancoPreguntas(QuestionRepository r) { this.repositorio = r; }

    public void almacenar(Question q)              { repositorio.guardar(q); }

    /**
     * Convierte una pregunta generada al modelo de dominio en estado de borrador.
     *
     * @param question pregunta generada que se va a almacenar
     */
    public void almacenar(core.model.Question question) {
        almacenar(question, new core.model.QuestionRequest(
                question.getTitle(), question.getContent(), question.getType()));
    }

    /**
     * Convierte y almacena una pregunta generada como borrador.
     *
     * @param question pregunta producida por un complemento
     * @param request datos de entrada y opciones de respuesta de la pregunta
     */
    public void almacenar(core.model.Question question,
                          core.model.QuestionRequest request) {
        if (question == null) {
            throw new IllegalArgumentException("La pregunta no puede ser null.");
        }
        almacenar(new Question(
                question.getTitle(),
                question.getContent(),
                request.getOptions(),
                request.getCorrectAnswer(),
                QuestionStatus.BORRADOR
        ));
    }

    public List<Question> listarTodas()            { return repositorio.listarTodas(); }
    public void actualizar(Question q)             { repositorio.actualizar(q); }
    /**
     * Archiva una pregunta conservando su registro en la base de datos.
     *
     * @param id identificador de la pregunta que se va a archivar
     */
    public void archivar(int id)                   { repositorio.archivar(id); }
}
