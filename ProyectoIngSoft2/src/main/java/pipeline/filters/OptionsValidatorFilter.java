package pipeline.filters;

import core.contracts.Filter;
import pipeline.PipelineContext;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OptionsValidatorFilter implements Filter {
    private static final int MIN_OPCIONES = 2;

    @Override
    public String getNombre() { return "Validación de opciones"; }

    @Override
    public void procesar(PipelineContext ctx) {
        List<String> opciones = ctx.getPregunta().getOpciones();
        if (opciones == null || opciones.size() < MIN_OPCIONES) {
            ctx.agregarError("Se requieren al menos " + MIN_OPCIONES + " opciones.");
            return;
        }
        if (opciones.stream().anyMatch(o -> o == null || o.isBlank())) {
            ctx.agregarError("Ninguna opción puede estar vacía.");
        }
        Set<String> unicas = new HashSet<>();
        for (String o : opciones) {
            if (!unicas.add(o.trim().toLowerCase())) {
                ctx.agregarError("Opción duplicada: " + o);
            }
        }
    }
}
