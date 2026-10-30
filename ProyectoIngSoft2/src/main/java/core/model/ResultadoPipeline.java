package core.model;

import model.Question;
import pipeline.PipelineContext;

import java.util.List;

public class ResultadoPipeline {
    private final boolean exitoso;
    private final List<String> errores;
    private final PipelineContext contexto;

    public ResultadoPipeline(boolean exitoso, List<String> errores, PipelineContext contexto) {
        this.exitoso = exitoso;
        this.errores = List.copyOf(errores);
        this.contexto = contexto;
    }

    public boolean isExitoso() { return exitoso; }
    public List<String> getErrores() { return errores; }
    public PipelineContext getContexto() { return contexto; }
    public PreguntaNueva getPregunta() { return contexto.getPregunta(); }
    public Question getPreguntaComoQuestion() { return getPregunta().comoQuestion(); }
}
