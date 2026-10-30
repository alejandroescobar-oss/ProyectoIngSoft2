package pipeline.filters;

import core.contracts.Filter;
import pipeline.PipelineContext;

public class ContentValidationFilter implements Filter {
    private static final int LONGITUD_MINIMA = 10;

    @Override
    public String getNombre() { return "Validación de contenido"; }

    @Override
    public void procesar(PipelineContext ctx) {
        String texto = ctx.getPregunta().getTexto();
        if (texto == null || texto.isBlank()) {
            ctx.agregarError("El texto de la pregunta está vacío.");
            return;
        }
        if (texto.trim().length() < LONGITUD_MINIMA) {
            ctx.agregarError("El texto debe tener al menos " + LONGITUD_MINIMA + " caracteres.");
        }
        if (!texto.trim().endsWith("?")) {
            ctx.agregarError("El texto debe formularse como pregunta (terminar en '?').");
        }
    }
}
