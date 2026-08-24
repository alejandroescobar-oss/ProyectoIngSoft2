package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Usuario;

public class DashboardView {

    private final Usuario usuario;
    private final service.UsuarioService usuarioService;

    public DashboardView(Usuario usuario, service.UsuarioService usuarioService) {
        this.usuario = usuario;
        this.usuarioService = usuarioService;
    }

    public void mostrar(Stage stage) {

        Label titulo = new Label("PANEL PRINCIPAL");

        Label bienvenida = new Label(
                "Bienvenido, " + usuario.getNombreCompleto()
        );

        Label rol = new Label(
                "Rol: " + usuario.getRol()
        );

        VBox opciones = new VBox(10);
        opciones.setAlignment(Pos.CENTER);

       MenuFactory menuFactory = new MenuFactory();

MenuRol menu =
        menuFactory.crearMenu(
                usuario.getRol(),
                usuario,
                usuarioService,
                stage
        );

menu.agregarOpciones(opciones);

        Button cerrarSesion = new Button("Cerrar sesión");

        cerrarSesion.setOnAction(event -> {

            LoginView loginView = new LoginView(
                    usuarioService
            );

            loginView.mostrar(stage);
        });

        VBox root = new VBox(20);

        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));

        root.getChildren().addAll(
                titulo,
                bienvenida,
                rol,
                opciones,
                cerrarSesion
        );

        Scene scene = new Scene(root, 600, 450);

        stage.setTitle(
                "Taller 2 - Panel de " + usuario.getRol()
        );

        stage.setScene(scene);
        stage.show();
    }

 


}