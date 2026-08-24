package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.EstadoUsuario;
import model.Rol;
import model.Usuario;

class UsuarioRepositorySQLiteTest {

    private UsuarioRepositorySQLite repository;

    @BeforeEach
    void configurarBaseDeDatos() {

        String url = "jdbc:sqlite:taller2-test.db";

        repository = new UsuarioRepositorySQLite(url);

        try (
                Connection connection =
                        DriverManager.getConnection(url);

                Statement statement =
                        connection.createStatement()
        ) {

            statement.execute("""
                    CREATE TABLE IF NOT EXISTS usuarios (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        login TEXT NOT NULL UNIQUE,
                        nombre_completo TEXT NOT NULL,
                        rol TEXT NOT NULL,
                        estado TEXT NOT NULL,
                        password_hash TEXT NOT NULL
                    )
                    """);

            statement.executeUpdate(
                    "DELETE FROM usuarios"
            );

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error preparando la base de datos de prueba.",
                    e
            );
        }
    }

    @Test
    void debeGuardarUsuario() {

        Usuario usuario = new Usuario(
                "juan",
                "Juan Pérez",
                Rol.ESTUDIANTE,
                EstadoUsuario.ACTIVO,
                "HASH_Abcdef1!"
        );

        repository.guardar(usuario);

        assertTrue(
                repository.existePorLogin("juan")
        );
    }

    @Test
    void debeBuscarUsuarioPorLogin() {

        Usuario usuario = new Usuario(
                "juan",
                "Juan Pérez",
                Rol.ESTUDIANTE,
                EstadoUsuario.ACTIVO,
                "HASH_Abcdef1!"
        );

        repository.guardar(usuario);

        Optional<Usuario> resultado =
                repository.buscarPorLogin("juan");

        assertTrue(resultado.isPresent());

        assertEquals(
                "Juan Pérez",
                resultado.get().getNombreCompleto()
        );

        assertEquals(
                Rol.ESTUDIANTE,
                resultado.get().getRol()
        );
    }

    @Test
    void debeListarUsuarios() {

        repository.guardar(
                new Usuario(
                        "juan",
                        "Juan Pérez",
                        Rol.ESTUDIANTE,
                        EstadoUsuario.ACTIVO,
                        "HASH_1"
                )
        );

        repository.guardar(
                new Usuario(
                        "maria",
                        "María López",
                        Rol.DOCENTE,
                        EstadoUsuario.ACTIVO,
                        "HASH_2"
                )
        );

        List<Usuario> usuarios =
                repository.listarTodos();

        assertEquals(
                2,
                usuarios.size()
        );
    }

    @Test
    void debeActualizarUsuario() {

        Usuario usuario = new Usuario(
                "juan",
                "Juan Pérez",
                Rol.ESTUDIANTE,
                EstadoUsuario.ACTIVO,
                "HASH_Abcdef1!"
        );

        repository.guardar(usuario);

        Usuario guardado =
                repository.buscarPorLogin("juan")
                        .orElseThrow();

        guardado.setNombreCompleto(
                "Juan Carlos Pérez"
        );

        guardado.setRol(
                Rol.DOCENTE
        );

        repository.actualizar(guardado);

        Usuario actualizado =
                repository.buscarPorLogin("juan")
                        .orElseThrow();

        assertEquals(
                "Juan Carlos Pérez",
                actualizado.getNombreCompleto()
        );

        assertEquals(
                Rol.DOCENTE,
                actualizado.getRol()
        );
    }

    @Test
    void debeEliminarUsuario() {

        Usuario usuario = new Usuario(
                "juan",
                "Juan Pérez",
                Rol.ESTUDIANTE,
                EstadoUsuario.ACTIVO,
                "HASH_Abcdef1!"
        );

        repository.guardar(usuario);

        Usuario guardado =
                repository.buscarPorLogin("juan")
                        .orElseThrow();

        repository.eliminar(
                guardado.getId()
        );

        assertFalse(
                repository.existePorLogin("juan")
        );
    }
}