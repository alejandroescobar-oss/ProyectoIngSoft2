package core.model;

import model.Question;
import model.QuestionStatus;

import java.util.ArrayList;
import java.util.List;

public class PreguntaNueva {
    private final String nombre;
    private final String texto;
    private final List<String> opciones;
    private final int respuestaCorrecta;

    public PreguntaNueva(String nombre, String texto, List<String> opciones, int respuestaCorrecta) {
        this.nombre = nombre;
        this.texto = texto;
        this.opciones = opciones == null ? new ArrayList<>() : new ArrayList<>(opciones);
        this.respuestaCorrecta = respuestaCorrecta;
    }

    public String getNombre() { return nombre; }
    public String getTexto() { return texto; }
    public List<String> getOpciones() { return new ArrayList<>(opciones); }
    public int getRespuestaCorrecta() { return respuestaCorrecta; }

    public Question comoQuestion() {
        return new Question(nombre, texto, opciones, respuestaCorrecta, QuestionStatus.PENDIENTE_REVISION);
    }
}
