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

    public void almacenar(core.model.Question question) {
        almacenar(question, new core.model.QuestionRequest(
                question.getTitle(), question.getContent(), question.getType()));
    }

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
                QuestionStatus.PENDIENTE_REVISION
        ));
    }

    public List<Question> listarTodas()            { return repositorio.listarTodas(); }
    public void actualizar(Question q)             { repositorio.actualizar(q); }
    public void eliminar(int id)                   { repositorio.eliminar(id); }
}
