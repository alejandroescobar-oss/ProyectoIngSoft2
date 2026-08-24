package ui;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class MenuDocente implements MenuRol {

    @Override
    public void agregarOpciones(VBox opciones) {

        Button gestionarEvaluaciones =
                new Button("Gestionar evaluaciones");

        Button verPreguntas =
                new Button("Ver preguntas");

        opciones.getChildren().addAll(
                gestionarEvaluaciones,
                verPreguntas
        );
    }
}