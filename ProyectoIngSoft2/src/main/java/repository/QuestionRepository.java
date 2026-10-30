package repository;

import model.Question;
import model.QuestionStatus;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository {
    void guardar(Question question);
    Optional<Question> buscarPorId(int id);
    List<Question> listarTodas();
    void actualizar(Question question);
    /**
     * Archiva una pregunta sin eliminar físicamente su registro.
     *
     * @param id identificador de la pregunta que se va a archivar
     */
    void archivar(int id);
    List<Question> listarPorEstado(QuestionStatus estado);
}
