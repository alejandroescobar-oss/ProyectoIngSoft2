package ui;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class MenuAutorPreguntas implements MenuRol {

    @Override
    public void agregarOpciones(VBox opciones) {

        Button crearPreguntas =
                new Button("Crear preguntas");

        Button misPreguntas =
                new Button("Mis preguntas");

        opciones.getChildren().addAll(
                crearPreguntas,
                misPreguntas
        );
    }
}