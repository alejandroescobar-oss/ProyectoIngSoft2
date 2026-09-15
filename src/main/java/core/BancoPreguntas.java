package core;

import model.Question;
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
    public List<Question> listarTodas()            { return repositorio.listarTodas(); }
    public void actualizar(Question q)             { repositorio.actualizar(q); }
    public void eliminar(int id)                   { repositorio.eliminar(id); }
}
