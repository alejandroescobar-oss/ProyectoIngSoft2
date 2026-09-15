package core;

import core.contracts.Plugin;
import core.model.PreguntaNueva;
import core.model.ResultadoPipeline;

public class LifecycleManager {
    private final PluginLoader loader;
    private final PluginRegistry registry;

    public LifecycleManager(PluginLoader loader, PluginRegistry registry) {
        this.loader = loader;
        this.registry = registry;
    }

    public void iniciar(String properties) {
        for (Plugin plugin : loader.cargarTodos(properties)) {
            try {
                registry.registrar(plugin);
            } catch (RuntimeException exception) {
                System.err.println("No se pudo registrar el plugin "
                        + plugin.getId() + ": " + exception.getMessage());
            }
        }
    }

    public ResultadoPipeline ejecutar(String pluginId, PreguntaNueva entrada) {
        Plugin p = registry.obtener(pluginId);
        if (p == null) throw new IllegalArgumentException("Plugin no registrado: " + pluginId);
        return p.ejecutar(entrada);
    }

    public void detener(String pluginId) {
        registry.desregistrar(pluginId);
    }

    public java.util.Collection<Plugin> listarPlugins() {
        return registry.listar();
    }
}
