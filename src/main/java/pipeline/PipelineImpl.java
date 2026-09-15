package pipeline;

import core.contracts.Filter;
import core.model.PreguntaNueva;
import core.model.ResultadoPipeline;

import java.util.List;

public class PipelineImpl implements core.contracts.Pipeline {
    private final List<Filter> filtros;

    public PipelineImpl(List<Filter> filtros) { this.filtros = filtros; }

    @Override
    public ResultadoPipeline ejecutar(PreguntaNueva entrada) {
        PipelineContext ctx = new PipelineContext(entrada);
        for (Filter f : filtros) {
            f.procesar(ctx);
            if (!ctx.isExitoso() && f.esCritico()) break;  // corta la tubería
        }
        return new ResultadoPipeline(ctx.isExitoso(), ctx.getErrores(), ctx);
    }
}
