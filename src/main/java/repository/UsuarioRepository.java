package repository;

import java.util.List;
import java.util.Optional;

import model.Usuario;

public interface UsuarioRepository {

    void guardar(Usuario usuario);

    Optional<Usuario> buscarPorLogin(String login);

    List<Usuario> listarTodos();

    boolean existePorLogin(String login);

    void actualizar(Usuario usuario);

    void eliminar(int id);
}