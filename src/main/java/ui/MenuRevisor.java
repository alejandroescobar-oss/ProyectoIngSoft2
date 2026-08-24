package ui;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class MenuRevisor implements MenuRol {

    @Override
    public void agregarOpciones(VBox opciones) {

        Button revisarPreguntas =
                new Button("Revisar preguntas");

        Button preguntasPendientes =
                new Button("Preguntas pendientes");

        opciones.getChildren().addAll(
                revisarPreguntas,
                preguntasPendientes
        );
    }
}