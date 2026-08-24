package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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

public class EditarUsuarioView {

    private final Usuario usuario;
    private final UsuarioService usuarioService;
    private final Runnable alGuardar;

    public EditarUsuarioView(
            Usuario usuario,
            UsuarioService usuarioService,
            Runnable alGuardar) {

        this.usuario = usuario;
        this.usuarioService = usuarioService;
        this.alGuardar = alGuardar;
    }

    public void mostrar(Stage stage) {

        Label titulo =
                new Label("EDITAR USUARIO");

        // LOGIN
        Label loginLabel =
                new Label("Usuario:");

        Label login =
                new Label(usuario.getLogin());

        // NOMBRE
        Label nombreLabel =
                new Label("Nombre completo:");

        TextField nombreField =
                new TextField();

        nombreField.setText(
                usuario.getNombreCompleto()
        );

        // ROL
        Label rolLabel =
                new Label("Rol:");

        ComboBox<Rol> rolCombo =
                new ComboBox<>();

        rolCombo.getItems().addAll(
                Rol.values()
        );

        rolCombo.setValue(
                usuario.getRol()
        );

        // ESTADO
        Label estadoLabel =
                new Label("Estado:");

        ComboBox<EstadoUsuario> estadoCombo =
                new ComboBox<>();

        estadoCombo.getItems().addAll(
                EstadoUsuario.values()
        );

        estadoCombo.setValue(
                usuario.getEstado()
        );

        // CONTRASEÑA
        Label passwordLabel =
                new Label("Nueva contraseña:");

        PasswordField passwordField =
                new PasswordField();

        passwordField.setPromptText(
                "Dejar vacío para mantener la actual"
        );

        // BOTONES
        Button guardar =
                new Button("Guardar cambios");

        Button cancelar =
                new Button("Cancelar");

        guardar.setOnAction(event -> {

            try {

                String nombre =
                        nombreField.getText().trim();

                if (nombre.isEmpty()) {

                    mostrarMensaje(
                            "El nombre completo es obligatorio.",
                            Alert.AlertType.WARNING
                    );

                    return;
                }

                usuario.setNombreCompleto(nombre);

                usuario.setRol(
                        rolCombo.getValue()
                );

                usuario.setEstado(
                        estadoCombo.getValue()
                );

                String nuevaPassword =
                        passwordField.getText();

                usuarioService.actualizarUsuario(
                        usuario,
                        nuevaPassword
                );

                mostrarMensaje(
                        "Usuario actualizado correctamente.",
                        Alert.AlertType.INFORMATION
                );

                if (alGuardar != null) {
                    alGuardar.run();
                }

                stage.close();

            } catch (IllegalArgumentException e) {

                mostrarMensaje(
                        e.getMessage(),
                        Alert.AlertType.WARNING
                );

            } catch (Exception e) {

                mostrarMensaje(
                        "Error al actualizar el usuario.",
                        Alert.AlertType.ERROR
                );
            }
        });

        cancelar.setOnAction(event -> {

            stage.close();
        });

        GridPane formulario =
                new GridPane();

        formulario.setHgap(10);
        formulario.setVgap(12);
        formulario.setAlignment(Pos.CENTER);

        formulario.add(loginLabel, 0, 0);
        formulario.add(login, 1, 0);

        formulario.add(nombreLabel, 0, 1);
        formulario.add(nombreField, 1, 1);

        formulario.add(rolLabel, 0, 2);
        formulario.add(rolCombo, 1, 2);

        formulario.add(estadoLabel, 0, 3);
        formulario.add(estadoCombo, 1, 3);

        formulario.add(passwordLabel, 0, 4);
        formulario.add(passwordField, 1, 4);

        VBox root =
                new VBox(20);

        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));

        root.getChildren().addAll(
                titulo,
                formulario,
                guardar,
                cancelar
        );

        Scene scene =
                new Scene(root, 600, 450);

        stage.setTitle(
                "Editar usuario"
        );

        stage.setScene(scene);
        stage.show();
    }

    private void mostrarMensaje(
            String mensaje,
            Alert.AlertType tipo) {

        Alert alert =
                new Alert(tipo);

        alert.setTitle("Editar usuario");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        alert.showAndWait();
    }
}