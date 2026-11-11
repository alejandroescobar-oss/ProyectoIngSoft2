package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import model.EstadoUsuario;
import model.Rol;
import model.Usuario;
import database.ConnectionProvider;
import database.SQLiteConnectionProvider;

public class UsuarioRepositorySQLite implements UsuarioRepository {
    private final ConnectionProvider connectionProvider;

    public UsuarioRepositorySQLite() {
        this(new SQLiteConnectionProvider());
    }

    public UsuarioRepositorySQLite(String databaseUrl) {
        this(new SQLiteConnectionProvider(databaseUrl));
    }

    public UsuarioRepositorySQLite(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }
    
    @Override
    public void guardar(Usuario usuario) {

        String sql = """
                INSERT INTO usuarios
                (login, nombre_completo, rol, estado, password_hash)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (
                Connection connection = connectionProvider.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, usuario.getLogin());
            statement.setString(2, usuario.getNombreCompleto());
            statement.setString(3, usuario.getRol().name());
            statement.setString(4, usuario.getEstado().name());
            statement.setString(5, usuario.getPasswordHash());

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error al guardar usuario.",
                    e
            );
        }
    }

    @Override
    public Optional<Usuario> buscarPorLogin(String login) {

        String sql = """
                SELECT id,
                       login,
                       nombre_completo,
                       rol,
                       estado,
                       password_hash
                FROM usuarios
                WHERE login = ?
                """;

        try (
                Connection connection = connectionProvider.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, login);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    Usuario usuario = new Usuario(
                            resultSet.getInt("id"),
                            resultSet.getString("login"),
                            resultSet.getString("nombre_completo"),
                            Rol.valueOf(resultSet.getString("rol")),
                            EstadoUsuario.valueOf(
                                    resultSet.getString("estado")
                            ),
                            resultSet.getString("password_hash")
                    );

                    return Optional.of(usuario);
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error al buscar usuario.",
                    e
            );
        }

        return Optional.empty();
    }

    @Override
    public List<Usuario> listarTodos() {

        List<Usuario> usuarios = new ArrayList<>();

        String sql = """
                SELECT id,
                       login,
                       nombre_completo,
                       rol,
                       estado,
                       password_hash
                FROM usuarios
                """;

        try (
                Connection connection = connectionProvider.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {

                Usuario usuario = new Usuario(
                        resultSet.getInt("id"),
                        resultSet.getString("login"),
                        resultSet.getString("nombre_completo"),
                        Rol.valueOf(resultSet.getString("rol")),
                        EstadoUsuario.valueOf(
                                resultSet.getString("estado")
                        ),
                        resultSet.getString("password_hash")
                );

                usuarios.add(usuario);
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error al listar usuarios.",
                    e
            );
        }

        return usuarios;
    }

    @Override
    public boolean existePorLogin(String login) {

        String sql = """
                SELECT COUNT(*)
                FROM usuarios
                WHERE login = ?
                """;

        try (
                Connection connection = connectionProvider.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, login);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next()
                        && resultSet.getInt(1) > 0;
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error al verificar el login.",
                    e
            );
        }
    }
        @Override
        public void actualizar(Usuario usuario) {

        String sql = """
            UPDATE usuarios
            SET nombre_completo = ?,
                rol = ?,
                estado = ?,
                password_hash = ?
            WHERE id = ?
            """;

         try (
            Connection connection = connectionProvider.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(sql)
         ) {

        statement.setString(1, usuario.getNombreCompleto());
        statement.setString(2, usuario.getRol().name());
        statement.setString(3, usuario.getEstado().name());
        statement.setString(4, usuario.getPasswordHash());
        statement.setInt(5, usuario.getId());

        statement.executeUpdate();

    } catch (SQLException e) {

        throw new RuntimeException(
                "Error al actualizar usuario.",
                e
        );
    }
}

@Override
public void eliminar(int id) {

    String sql = """
            DELETE FROM usuarios
            WHERE id = ?
            """;

    try (
            Connection connection = connectionProvider.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(sql)
    ) {

        statement.setInt(1, id);

        statement.executeUpdate();

    } catch (SQLException e) {

        throw new RuntimeException(
                "Error al eliminar usuario.",
                e
        );
    }
}
    
}
