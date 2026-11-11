package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import model.EstadoUsuario;
import model.Rol;
import model.Usuario;
import repository.UsuarioRepository;
import security.PasswordHasher;
import validation.PasswordValidator;

class UsuarioServiceTest {

    /*
     * Repositorio falso para las pruebas.
     * No utiliza SQLite.
     */
    static class FakeUsuarioRepository implements UsuarioRepository {

        private final List<Usuario> usuarios = new ArrayList<>();

        @Override
        public void guardar(Usuario usuario) {
            usuarios.add(usuario);
        }
       
@Override
public void actualizar(Usuario usuario) {

    for (int i = 0; i < usuarios.size(); i++) {

        if (usuarios.get(i).getId() == usuario.getId()) {
            usuarios.set(i, usuario);
            return;
        }
    }
}

@Override
public void eliminar(int id) {
    usuarios.removeIf(usuario -> usuario.getId() == id);
}

        @Override
        public Optional<Usuario> buscarPorLogin(String login) {

            return usuarios.stream()
                    .filter(u -> u.getLogin().equals(login))
                    .findFirst();
        }

        @Override
        public List<Usuario> listarTodos() {
            return usuarios;
        }

        @Override
        public boolean existePorLogin(String login) {

            return usuarios.stream()
                    .anyMatch(u -> u.getLogin().equals(login));
        }
    }

    /*
     * PasswordHasher falso para las pruebas.
     */
    static class FakePasswordHasher implements PasswordHasher {

        @Override
        public String hash(String password) {
            return "HASH_" + password;
        }

        @Override
        public boolean verificar(String password, String hash) {
            return hash.equals("HASH_" + password);
        }
    }

    @Test
    void debeRegistrarUsuarioCorrectamente() {

        FakeUsuarioRepository repository =
                new FakeUsuarioRepository();

        PasswordValidator validator =
                new PasswordValidator();

        PasswordHasher hasher =
                new FakePasswordHasher();

        UsuarioService service =
                new UsuarioService(
                        repository,
                        validator,
                        hasher
                );

        Usuario usuario = new Usuario(
                "juan",
                "Juan Pérez",
                Rol.ESTUDIANTE,
                EstadoUsuario.ACTIVO,
                null
        );

        service.registrarUsuario(
                usuario,
                "Abcdef1!"
        );

        assertTrue(
                repository.existePorLogin("juan")
        );

        assertEquals(
                "HASH_Abcdef1!",
                usuario.getPasswordHash()
        );
    }

    @Test
    void debeRechazarLoginDuplicado() {

        FakeUsuarioRepository repository =
                new FakeUsuarioRepository();

        PasswordValidator validator =
                new PasswordValidator();

        PasswordHasher hasher =
                new FakePasswordHasher();

        UsuarioService service =
                new UsuarioService(
                        repository,
                        validator,
                        hasher
                );

        Usuario usuario1 = new Usuario(
                "juan",
                "Juan Pérez",
                Rol.ESTUDIANTE,
                EstadoUsuario.ACTIVO,
                null
        );

        service.registrarUsuario(
                usuario1,
                "Abcdef1!"
        );

        Usuario usuario2 = new Usuario(
                "juan",
                "Otro Usuario",
                Rol.DOCENTE,
                EstadoUsuario.ACTIVO,
                null
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.registrarUsuario(
                        usuario2,
                        "Abcdef1!"
                )
        );
    }

    @Test
    void debeRechazarPasswordInvalida() {

        FakeUsuarioRepository repository =
                new FakeUsuarioRepository();

        PasswordValidator validator =
                new PasswordValidator();

        PasswordHasher hasher =
                new FakePasswordHasher();

        UsuarioService service =
                new UsuarioService(
                        repository,
                        validator,
                        hasher
                );

        Usuario usuario = new Usuario(
                "pedro",
                "Pedro Gómez",
                Rol.DOCENTE,
                EstadoUsuario.ACTIVO,
                null
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.registrarUsuario(
                        usuario,
                        "abc"
                )
        );
    }

    @Test
    void debeIniciarSesionCorrectamente() {

        FakeUsuarioRepository repository =
                new FakeUsuarioRepository();

        PasswordValidator validator =
                new PasswordValidator();

        PasswordHasher hasher =
                new FakePasswordHasher();

        UsuarioService service =
                new UsuarioService(
                        repository,
                        validator,
                        hasher
                );

        Usuario usuario = new Usuario(
                "alejandro",
                "Alejandro Fajardo",
                Rol.ESTUDIANTE,
                EstadoUsuario.ACTIVO,
                "HASH_Abcdef1!"
        );

        repository.guardar(usuario);

        Usuario resultado =
                service.iniciarSesion(
                        "alejandro",
                        "Abcdef1!"
                );

        assertNotNull(resultado);

        assertEquals(
                "Alejandro Fajardo",
                resultado.getNombreCompleto()
        );

        assertEquals(
                Rol.ESTUDIANTE,
                resultado.getRol()
        );
    }

    @Test
    void debeRechazarUsuarioInactivo() {

        FakeUsuarioRepository repository =
                new FakeUsuarioRepository();

        PasswordValidator validator =
                new PasswordValidator();

        PasswordHasher hasher =
                new FakePasswordHasher();

        UsuarioService service =
                new UsuarioService(
                        repository,
                        validator,
                        hasher
                );

        Usuario usuario = new Usuario(
                "inactivo",
                "Usuario Inactivo",
                Rol.ESTUDIANTE,
                EstadoUsuario.INACTIVO,
                "HASH_Abcdef1!"
        );

        repository.guardar(usuario);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> service.iniciarSesion(
                                "inactivo",
                                "Abcdef1!"
                        )
                );

        assertEquals(
                "El usuario está inactivo.",
                exception.getMessage()
        );
    }

    @Test
    void debeRechazarPasswordIncorrecta() {

        FakeUsuarioRepository repository =
                new FakeUsuarioRepository();

        PasswordValidator validator =
                new PasswordValidator();

        PasswordHasher hasher =
                new FakePasswordHasher();

        UsuarioService service =
                new UsuarioService(
                        repository,
                        validator,
                        hasher
                );

        Usuario usuario = new Usuario(
                "alejandro",
                "Alejandro Fajardo",
                Rol.ESTUDIANTE,
                EstadoUsuario.ACTIVO,
                "HASH_Abcdef1!"
        );

        repository.guardar(usuario);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.iniciarSesion(
                        "alejandro",
                        "PasswordIncorrecta1!"
                )
        );
    }
    @Test
void debeActualizarUsuarioCorrectamente() {

    FakeUsuarioRepository repository =
            new FakeUsuarioRepository();

    PasswordValidator validator =
            new PasswordValidator();

    PasswordHasher hasher =
            new FakePasswordHasher();

    UsuarioService service =
            new UsuarioService(
                    repository,
                    validator,
                    hasher
            );

    Usuario usuario = new Usuario(
            1,
            "juan",
            "Juan Pérez",
            Rol.ESTUDIANTE,
            EstadoUsuario.ACTIVO,
            "HASH_Abcdef1!"
    );

    repository.guardar(usuario);

    usuario.setNombreCompleto("Juan Carlos Pérez");
    usuario.setRol(Rol.DOCENTE);

    service.actualizarUsuario(usuario);

    assertEquals(
            "Juan Carlos Pérez",
            usuario.getNombreCompleto()
    );

    assertEquals(
            Rol.DOCENTE,
            usuario.getRol()
    );
}
@Test
void debeActualizarPasswordCorrectamente() {

    FakeUsuarioRepository repository =
            new FakeUsuarioRepository();

    PasswordValidator validator =
            new PasswordValidator();

    PasswordHasher hasher =
            new FakePasswordHasher();

    UsuarioService service =
            new UsuarioService(
                    repository,
                    validator,
                    hasher
            );

    Usuario usuario = new Usuario(
            1,
            "juan",
            "Juan Pérez",
            Rol.ESTUDIANTE,
            EstadoUsuario.ACTIVO,
            "HASH_Abcdef1!"
    );

    repository.guardar(usuario);

    service.actualizarUsuario(
            usuario,
            "Nueva1!"
    );

    assertEquals(
            "HASH_Nueva1!",
            usuario.getPasswordHash()
    );
}

@Test
void debeRechazarNuevaPasswordInvalida() {

    FakeUsuarioRepository repository =
            new FakeUsuarioRepository();

    PasswordValidator validator =
            new PasswordValidator();

    PasswordHasher hasher =
            new FakePasswordHasher();

    UsuarioService service =
            new UsuarioService(
                    repository,
                    validator,
                    hasher
            );

    Usuario usuario = new Usuario(
            1,
            "juan",
            "Juan Pérez",
            Rol.ESTUDIANTE,
            EstadoUsuario.ACTIVO,
            "HASH_Abcdef1!"
    );

    repository.guardar(usuario);

    assertThrows(
            IllegalArgumentException.class,
            () -> service.actualizarUsuario(
                    usuario,
                    "abc"
            )
    );
}
@Test
void debeEliminarUsuarioCorrectamente() {

    FakeUsuarioRepository repository =
            new FakeUsuarioRepository();

    PasswordValidator validator =
            new PasswordValidator();

    PasswordHasher hasher =
            new FakePasswordHasher();

    UsuarioService service =
            new UsuarioService(
                    repository,
                    validator,
                    hasher
            );

    Usuario usuario = new Usuario(
            1,
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

    service.eliminarUsuario(1);

    assertFalse(
            repository.existePorLogin("juan")
    );
}
@Test
void debeListarUsuariosCorrectamente() {

    FakeUsuarioRepository repository =
            new FakeUsuarioRepository();

    PasswordValidator validator =
            new PasswordValidator();

    PasswordHasher hasher =
            new FakePasswordHasher();

    UsuarioService service =
            new UsuarioService(
                    repository,
                    validator,
                    hasher
            );

    repository.guardar(
            new Usuario(
                    1,
                    "juan",
                    "Juan Pérez",
                    Rol.ESTUDIANTE,
                    EstadoUsuario.ACTIVO,
                    "HASH_1"
            )
    );

    repository.guardar(
            new Usuario(
                    2,
                    "maria",
                    "María López",
                    Rol.DOCENTE,
                    EstadoUsuario.ACTIVO,
                    "HASH_2"
            )
    );

    List<Usuario> usuarios =
            service.listarUsuarios();

    assertEquals(2, usuarios.size());

    assertEquals(
            "Juan Pérez",
            usuarios.get(0).getNombreCompleto()
    );

    assertEquals(
            "María López",
            usuarios.get(1).getNombreCompleto()
    );
}

}