package ui;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class MenuEstudiante implements MenuRol {

    @Override
    public void agregarOpciones(VBox opciones) {

        Button presentarEvaluacion =
                new Button("Presentar evaluación");

        Button verResultados =
                new Button("Ver resultados");

        opciones.getChildren().addAll(
                presentarEvaluacion,
                verResultados
        );
    }
}