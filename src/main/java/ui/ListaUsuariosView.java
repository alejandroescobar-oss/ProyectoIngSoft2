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
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Usuario;
import service.UsuarioService;

public class ListaUsuariosView {

    private final UsuarioService usuarioService;

    private final ObservableList<Usuario> usuarios =
            FXCollections.observableArrayList();

    public ListaUsuariosView(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public void mostrar(Stage stage) {

        Label titulo =
                new Label("LISTA DE USUARIOS");

        ListView<Usuario> listaUsuarios =
                new ListView<>();

        listaUsuarios.setItems(usuarios);

        // Cargar usuarios al abrir la ventana
        cargarUsuarios();

        TextField campoBusqueda =
                new TextField();

        campoBusqueda.setPromptText(
                "Buscar usuario por Login..."
        );

        Button buscar =
                new Button("Buscar");

        Button mostrarTodos =
                new Button("Mostrar todos");

        Button volver =
                new Button("Volver");

        // BUSCAR
        buscar.setOnAction(event -> {

            String texto =
                    campoBusqueda.getText().trim();

            buscarUsuarios(texto);
        });

        // MOSTRAR TODOS
        mostrarTodos.setOnAction(event -> {

            campoBusqueda.clear();

            cargarUsuarios();
        });

        // VOLVER
        volver.setOnAction(event -> {

            // Aquí puedes colocar la vista a la que quieras regresar.
            stage.close();
        });

        // Barra de búsqueda
        HBox busqueda =
                new HBox(
                        10,
                        campoBusqueda,
                        buscar,
                        mostrarTodos
                );

        busqueda.setAlignment(Pos.CENTER);

        // Botón volver
        HBox botones =
                new HBox(
                        10,
                        volver
                );

        botones.setAlignment(Pos.CENTER);

        VBox root =
                new VBox(15);

        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(25));

        root.getChildren().addAll(
                titulo,
                busqueda,
                listaUsuarios,
                botones
        );

        Scene scene =
                new Scene(root, 700, 500);

        stage.setTitle(
                "Lista de usuarios"
        );

        stage.setScene(scene);
        stage.show();
    }

    /**
     * Carga todos los usuarios.
     */
    private void cargarUsuarios() {

        List<Usuario> lista =
                usuarioService.listarUsuarios();

        usuarios.setAll(lista);
    }

    /**
     * Busca usuarios por nombre completo.
     */
    private void buscarUsuarios(String nombre) {

        // Si está vacío, mostrar todos
        if (nombre.isEmpty()) {

            cargarUsuarios();
            return;
        }

        List<Usuario> lista =
                usuarioService.listarUsuarios();

        ObservableList<Usuario> resultados =
                FXCollections.observableArrayList();

        for (Usuario usuario : lista) {

            String Login =
                    usuario.getLogin();

            if (Login != null &&
                    Login
                            .toLowerCase()
                            .contains(nombre.toLowerCase())) {

                resultados.add(usuario);
            }
        }

        usuarios.setAll(resultados);

        if (resultados.isEmpty()) {

            mostrarMensaje(
                    "No se encontraron usuarios con ese nombre.",
                    Alert.AlertType.INFORMATION
            );
        }
    }

    private void mostrarMensaje(
            String mensaje,
            Alert.AlertType tipo) {

        Alert alert =
                new Alert(tipo);

        alert.setTitle(
                "Lista de usuarios"
        );

        alert.setHeaderText(null);

        alert.setContentText(
                mensaje
        );

        alert.showAndWait();
    }
}