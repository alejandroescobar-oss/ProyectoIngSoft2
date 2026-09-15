package pipeline;

import core.model.*;

import java.util.ArrayList;
import java.util.List;

public class PipelineContext {
    private PreguntaNueva pregunta;
    private Competencia competencia;
    private NivelDificultad nivel;
    private final List<String> errores = new ArrayList<>();
    private boolean exitoso = true;

    public PipelineContext(PreguntaNueva pregunta) { this.pregunta = pregunta; }

    public PreguntaNueva getPregunta() { return pregunta; }
    public Competencia getCompetencia() { return competencia; }
    public void setCompetencia(Competencia c) { this.competencia = c; }
    public NivelDificultad getNivel() { return nivel; }
    public void setNivel(NivelDificultad n) { this.nivel = n; }

    public void agregarError(String msg) {
        errores.add(msg);
        exitoso = false;
    }
    public List<String> getErrores() { return errores; }
    public boolean isExitoso() { return exitoso; }
}
