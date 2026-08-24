package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Usuario;
import service.UsuarioService;

public class LoginView {

    private final UsuarioService usuarioService;

    public LoginView(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public void mostrar(Stage stage) {

        Label titulo = new Label("INICIAR SESIÓN");

        Label usuarioLabel = new Label("Usuario");

        TextField usuarioField = new TextField();
        usuarioField.setPromptText("Ingrese su usuario");

        Label passwordLabel = new Label("Contraseña");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Ingrese su contraseña");

        Label mensaje = new Label();

        Button iniciarButton =
                new Button("INICIAR SESIÓN");
         Button registrarButton =
                new Button("REGISTRAR");


        iniciarButton.setOnAction(event -> {

            String login = usuarioField.getText();
            String password = passwordField.getText();

            try {

                Usuario usuario =
                        usuarioService.iniciarSesion(
                                login,
                                password
                        );

               DashboardView dashboardView =
        new DashboardView(
                usuario,
                usuarioService
        );

        dashboardView.mostrar(stage);

            } catch (IllegalArgumentException e) {

                mensaje.setText(e.getMessage());
            }
        });
        registrarButton.setOnAction(event -> {

    RegistroView registroView =
            new RegistroView(usuarioService);

    registroView.mostrar(stage);
});

        VBox root = new VBox(10);

        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);

    root.getChildren().addAll(
        titulo,
        usuarioLabel,
        usuarioField,
        passwordLabel,
        passwordField,
        iniciarButton,
        registrarButton,
        mensaje
);

        Scene scene =
                new Scene(root, 500, 400);

        stage.setTitle(
                "Taller 2 - Inicio de Sesión"
        );

        stage.setScene(scene);
        stage.show();
    }
}