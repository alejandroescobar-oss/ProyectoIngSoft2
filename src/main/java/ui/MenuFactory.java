package ui;

import javafx.stage.Stage;
import model.Rol;
import model.Usuario;
import service.UsuarioService;

public class MenuFactory {

    public MenuRol crearMenu(
            Rol rol,
            Usuario usuario,
            UsuarioService usuarioService,
            Stage stage) {

        return switch (rol) {

            case ADMINISTRADOR ->
                    new MenuAdministrador(
                            usuario,
                            usuarioService,
                            stage
                    );

            case AUTOR_PREGUNTAS ->
                    new MenuAutorPreguntas();

            case REVISOR ->
                    new MenuRevisor();

            case DOCENTE ->
                    new MenuDocente();

            case ESTUDIANTE ->
                    new MenuEstudiante();
        };
    }
}