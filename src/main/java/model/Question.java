package model;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private int id;
    private String nombre;
    private String texto;
    private List<String> opciones;
    private int respuestaCorrecta;
    private QuestionStatus estado;

    public Question(String nombre, String texto, List<String> opciones,
                    int respuestaCorrecta, QuestionStatus estado) {
        this(0, nombre, texto, opciones, respuestaCorrecta, estado);
    }

    public Question(int id, String nombre, String texto, List<String> opciones,
                    int respuestaCorrecta, QuestionStatus estado) {
        this.id = id;
        this.nombre = nombre;
        this.texto = texto;
        this.opciones = new ArrayList<>(opciones);
        this.respuestaCorrecta = respuestaCorrecta;
        this.estado = estado;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTexto() { return texto; }
    public List<String> getOpciones() { return new ArrayList<>(opciones); }
    public int getRespuestaCorrecta() { return respuestaCorrecta; }
    public QuestionStatus getEstado() { return estado; }

    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setTexto(String texto) { this.texto = texto; }
    public void setOpciones(List<String> opciones) { this.opciones = new ArrayList<>(opciones); }
    public void setRespuestaCorrecta(int respuestaCorrecta) { this.respuestaCorrecta = respuestaCorrecta; }
    public void setEstado(QuestionStatus estado) { this.estado = estado; }

    @Override
    public String toString() {
        return id + " - " + nombre;
    }
}
