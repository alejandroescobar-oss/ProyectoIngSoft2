package service;

import java.util.List;

import model.EstadoUsuario;
import model.Usuario;
import repository.UsuarioRepository;
import security.PasswordHasher;
import validation.PasswordValidator;

public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordValidator passwordValidator;
    private final PasswordHasher passwordHasher;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PasswordValidator passwordValidator,
            PasswordHasher passwordHasher
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordValidator = passwordValidator;
        this.passwordHasher = passwordHasher;
    }

    public void registrarUsuario(Usuario usuario, String password) {

        if (usuario == null) {
            throw new IllegalArgumentException(
                    "El usuario no puede ser nulo."
            );
        }

        if (!passwordValidator.esValida(password)) {
            throw new IllegalArgumentException(
                    "La contraseña no cumple los requisitos."
            );
        }

        if (usuarioRepository.existePorLogin(usuario.getLogin())) {
            throw new IllegalArgumentException(
                    "El login ya está registrado."
            );
        }

        String passwordHash = passwordHasher.hash(password);

        usuario.setPasswordHash(passwordHash);

        usuarioRepository.guardar(usuario);
    }
    

    public Usuario iniciarSesion(String login, String password) {

    Usuario usuario = usuarioRepository
            .buscarPorLogin(login)
            .orElseThrow(() ->
                    new IllegalArgumentException(
                            "Usuario o contraseña incorrectos."
                    )
            );

    if (usuario.getEstado() != EstadoUsuario.ACTIVO) {
        throw new IllegalArgumentException(
                "El usuario está inactivo."
        );
    }

    if (!passwordHasher.verificar(
            password,
            usuario.getPasswordHash()
    )) {
        throw new IllegalArgumentException(
                "Usuario o contraseña incorrectos."
        );
    }

    return usuario;
}
public void actualizarUsuario(Usuario usuario) {

    if (usuario == null) {
        throw new IllegalArgumentException(
                "El usuario no puede ser nulo."
        );
    }

    usuarioRepository.actualizar(usuario);
}

public void eliminarUsuario(int id) {

    usuarioRepository.eliminar(id);
}
public List<Usuario> listarUsuarios() {

    return usuarioRepository.listarTodos();
}
public void actualizarUsuario(
        Usuario usuario,
        String nuevaPassword) {

    if (usuario == null) {
        throw new IllegalArgumentException(
                "El usuario no puede ser nulo."
        );
    }

    if (nuevaPassword != null &&
            !nuevaPassword.isBlank()) {

        if (!passwordValidator.esValida(nuevaPassword)) {
            throw new IllegalArgumentException(
                    "La nueva contraseña no cumple los requisitos."
            );
        }

        String hash =
                passwordHasher.hash(nuevaPassword);

        usuario.setPasswordHash(hash);
    }

    usuarioRepository.actualizar(usuario);
}
}