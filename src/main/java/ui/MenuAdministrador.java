package ui;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Usuario;
import service.UsuarioService;

public class MenuAdministrador implements MenuRol {

    private final Usuario usuario;
    private final UsuarioService usuarioService;
    private final Stage stage;

    public MenuAdministrador(
            Usuario usuario,
            UsuarioService usuarioService,
            Stage stage) {

        this.usuario = usuario;
        this.usuarioService = usuarioService;
        this.stage = stage;
    }

    @Override
    public void agregarOpciones(VBox opciones) {

        Button gestionarUsuarios =
                new Button("Gestionar usuarios");

        Button verUsuarios =
                new Button("Ver usuarios");

        gestionarUsuarios.setOnAction(event -> {

            GestionUsuariosView vista =
                    new GestionUsuariosView(
                            usuario,
                            usuarioService
                    );

            vista.mostrar(stage);
        });

        verUsuarios.setOnAction(event -> {

            GestionUsuariosView vista =
                    new GestionUsuariosView(
                            usuario,
                            usuarioService
                    );

            vista.mostrar(stage);
        });

        opciones.getChildren().addAll(
                gestionarUsuarios,
                verUsuarios
        );
    }
}