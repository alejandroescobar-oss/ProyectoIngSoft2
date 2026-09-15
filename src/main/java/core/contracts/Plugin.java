package core.contracts;

import core.model.PreguntaNueva;
import core.model.ResultadoPipeline;

public interface Plugin {
    String getId();
    String getNombre();
    String getVersion();
    String getDescripcion();

    /** Se invoca UNA sola vez cuando el PluginLoader lo descubre. */
    void inicializar(PluginContext context);

    /** Se invoca cada vez que el usuario pide ejecutar el plugin. */
    ResultadoPipeline ejecutar(PreguntaNueva entrada);

    /** Libera recursos. El núcleo lo llama al desregistrar. */
    void detener();
}
