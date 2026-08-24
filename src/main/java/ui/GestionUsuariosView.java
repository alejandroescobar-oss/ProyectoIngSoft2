package ui;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Usuario;
import service.UsuarioService;

public class GestionUsuariosView {

    private final Usuario usuarioActual;
    private final UsuarioService usuarioService;

    private final ObservableList<Usuario> usuarios =
            FXCollections.observableArrayList();

    public GestionUsuariosView(
            Usuario usuarioActual,
            UsuarioService usuarioService) {

        this.usuarioActual = usuarioActual;
        this.usuarioService = usuarioService;
    }

    public void mostrar(Stage stage) {

        Label titulo =
                new Label("GESTIÓN DE USUARIOS");

        ListView<Usuario> listaUsuarios =
                new ListView<>();

        listaUsuarios.setItems(usuarios);

        cargarUsuarios();

        Button modificar =
                new Button("Modificar");

        Button eliminar =
                new Button("Eliminar");

        Button volver =
                new Button("Volver");

        modificar.setOnAction(event -> {

            Usuario seleccionado =
                    listaUsuarios.getSelectionModel()
                            .getSelectedItem();

            if (seleccionado == null) {

                mostrarMensaje(
                        "Seleccione un usuario.",
                        Alert.AlertType.WARNING
                );

                return;
            }

            EditarUsuarioView editar =
                    new EditarUsuarioView(
                            seleccionado,
                            usuarioService,
                            () -> cargarUsuarios()
                    );

            editar.mostrar(stage);
        });

        eliminar.setOnAction(event -> {

            Usuario seleccionado =
                    listaUsuarios.getSelectionModel()
                            .getSelectedItem();

            if (seleccionado == null) {

                mostrarMensaje(
                        "Seleccione un usuario.",
                        Alert.AlertType.WARNING
                );

                return;
            }

            // Evitamos que el administrador se elimine a sí mismo
            if (seleccionado.getId() == usuarioActual.getId()) {

                mostrarMensaje(
                        "No puede eliminar su propio usuario.",
                        Alert.AlertType.WARNING
                );

                return;
            }

            try {

                usuarioService.eliminarUsuario(
                        seleccionado.getId()
                );

                cargarUsuarios();

                mostrarMensaje(
                        "Usuario eliminado correctamente.",
                        Alert.AlertType.INFORMATION
                );

            } catch (Exception e) {

                mostrarMensaje(
                        "No se pudo eliminar el usuario.",
                        Alert.AlertType.ERROR
                );
            }
        });

        volver.setOnAction(event -> {

            DashboardView dashboard =
                    new DashboardView(
                            usuarioActual,
                            usuarioService
                    );

            dashboard.mostrar(stage);
        });

        HBox botones =
                new HBox(
                        10,
                        modificar,
                        eliminar,
                        volver
                );

        botones.setAlignment(Pos.CENTER);

        VBox root =
                new VBox(15);

        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(25));

        root.getChildren().addAll(
                titulo,
                listaUsuarios,
                botones
        );

        Scene scene =
                new Scene(root, 700, 500);

        stage.setTitle(
                "Gestión de usuarios"
        );

        stage.setScene(scene);
        stage.show();
    }

    private void cargarUsuarios() {

        List<Usuario> lista =
                usuarioService.listarUsuarios();

        usuarios.setAll(lista);
    }

    private void mostrarMensaje(
            String mensaje,
            Alert.AlertType tipo) {

        Alert alert =
                new Alert(tipo);

        alert.setTitle("Gestión de usuarios");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        alert.showAndWait();
    }
}