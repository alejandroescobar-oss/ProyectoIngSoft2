package pipeline.filters;

import core.contracts.Filter;
import pipeline.PipelineContext;

import java.util.List;

public class CorrectAnswerValidatorFilter implements Filter {
    @Override public String getNombre() { return "Validación de respuesta correcta"; }

    @Override
    public void procesar(PipelineContext ctx) {
        int idx = ctx.getPregunta().getRespuestaCorrecta();
        List<String> opciones = ctx.getPregunta().getOpciones();

        if (opciones == null || opciones.isEmpty()) {
            ctx.agregarError("No hay opciones para validar la respuesta.");
            return;
        }
        if (idx < 0 || idx >= opciones.size()) {
            ctx.agregarError("El índice de respuesta correcta está fuera de rango.");
        }
        if (ctx.getCompetencia() == null) {
            ctx.agregarError("La pregunta no fue clasificada antes de validar la respuesta.");
        }
    }
}
