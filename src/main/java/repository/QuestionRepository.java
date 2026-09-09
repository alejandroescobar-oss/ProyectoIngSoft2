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
    void eliminar(int id);
    List<Question> listarPorEstado(QuestionStatus estado);
}
