package pipeline.filters;

import core.contracts.Filter;
import core.model.Competencia;
import core.model.NivelDificultad;
import pipeline.PipelineContext;

public class ClassificationFilter implements Filter {
    @Override
    public String getNombre() { return "Clasificación de competencia"; }

    @Override
    public void procesar(PipelineContext ctx) {
        String texto = ctx.getPregunta().getTexto().toLowerCase();

        // Clasificación heurística (un plugin real usaría IA o reglas más ricas)
        Competencia comp;
        if (texto.contains("arquitectura") || texto.contains("patrón")
                || texto.contains("microservicio") || texto.contains("solid")) {
            comp = Competencia.ARQUITECTURA_SOFTWARE;
        } else if (texto.contains("algoritmo") || texto.contains("complejidad")) {
            comp = Competencia.ALGORITMOS;
        } else if (texto.contains("base de datos") || texto.contains("sql")) {
            comp = Competencia.BASES_DE_DATOS;
        } else {
            comp = Competencia.GENERAL;
        }

        NivelDificultad nivel = texto.length() > 150
                ? NivelDificultad.AVANZADO
                : texto.length() > 60
                ? NivelDificultad.INTERMEDIO
                : NivelDificultad.BASICO;

        ctx.setCompetencia(comp);
        ctx.setNivel(nivel);
    }
}
