package core.contracts;

import pipeline.PipelineContext;

public interface Filter {
    String getNombre();
    /** Modifica el contexto o añade errores. */
    void procesar(PipelineContext context);
    default boolean esCritico() { return true; }
}
