package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.EstadoUsuario;
import model.Rol;
import model.Usuario;
import service.UsuarioService;

public class RegistroView {

    private final UsuarioService usuarioService;

    public RegistroView(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public void mostrar(Stage stage) {

        Label titulo = new Label("REGISTRAR USUARIO");

        Label loginLabel = new Label("Nombre de usuario:");
        TextField loginField = new TextField();

        Label nombreLabel = new Label("Nombre completo:");
        TextField nombreField = new TextField();

        Label rolLabel = new Label("Rol:");
        ComboBox<Rol> rolComboBox = new ComboBox<>();

        rolComboBox.getItems().addAll(Rol.values());
        rolComboBox.setValue(Rol.ESTUDIANTE);

        Label estadoLabel = new Label("Estado:");
        ComboBox<EstadoUsuario> estadoComboBox =
                new ComboBox<>();

        estadoComboBox.getItems().addAll(
                EstadoUsuario.values()
        );

        estadoComboBox.setValue(
                EstadoUsuario.ACTIVO
        );

        Label passwordLabel = new Label("Contraseña:");
        PasswordField passwordField = new PasswordField();

        Label confirmarLabel = new Label("Confirmar contraseña:");
        PasswordField confirmarField = new PasswordField();

        Label mensaje = new Label();

        Button registrarButton =
                new Button("REGISTRAR");

        Button volverButton =
                new Button("VOLVER");

        registrarButton.setOnAction(event -> {

            String login = loginField.getText().trim();
            String nombre = nombreField.getText().trim();
            Rol rol = rolComboBox.getValue();
            EstadoUsuario estado =
                    estadoComboBox.getValue();

            String password =
                    passwordField.getText();

            String confirmar =
                    confirmarField.getText();

            if (!password.equals(confirmar)) {

                mensaje.setText(
                        "Las contraseñas no coinciden."
                );

                return;
            }

            try {

               Usuario usuario = new Usuario(
        login,
        nombre,
        rol,
        estado,
        null
);

usuarioService.registrarUsuario(
        usuario,
        password
);

                mensaje.setText(
                        "Usuario registrado correctamente."
                );

                loginField.clear();
                nombreField.clear();
                passwordField.clear();
                confirmarField.clear();

            } catch (IllegalArgumentException e) {

                mensaje.setText(e.getMessage());

            } catch (Exception e) {

                mensaje.setText(
                        "Error al registrar el usuario."
                );
            }
        });

        volverButton.setOnAction(event -> {

            LoginView loginView =
                    new LoginView(usuarioService);

            loginView.mostrar(stage);
        });

        GridPane formulario = new GridPane();

        formulario.setHgap(10);
        formulario.setVgap(10);

        formulario.add(loginLabel, 0, 0);
        formulario.add(loginField, 1, 0);

        formulario.add(nombreLabel, 0, 1);
        formulario.add(nombreField, 1, 1);

        formulario.add(rolLabel, 0, 2);
        formulario.add(rolComboBox, 1, 2);

        formulario.add(estadoLabel, 0, 3);
        formulario.add(estadoComboBox, 1, 3);

        formulario.add(passwordLabel, 0, 4);
        formulario.add(passwordField, 1, 4);

        formulario.add(confirmarLabel, 0, 5);
        formulario.add(confirmarField, 1, 5);

        VBox botones = new VBox(
                10,
                registrarButton,
                volverButton
        );

        botones.setAlignment(Pos.CENTER);

        VBox root = new VBox(
                20,
                titulo,
                formulario,
                botones,
                mensaje
        );

        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);

        Scene scene =
                new Scene(root, 600, 500);

        stage.setTitle(
                "Taller 2 - Registrar Usuario"
        );

        stage.setScene(scene);
        stage.show();
    }
}