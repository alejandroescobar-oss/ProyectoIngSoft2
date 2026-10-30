package core;

import core.contracts.QuestionPlugin;
import core.contracts.PluginCatalog;
import core.contracts.PluginSource;

public class LifecycleManager {
    private final PluginSource loader;
    private final PluginCatalog registry;

    public LifecycleManager(PluginSource loader, PluginCatalog registry) {
        this.loader = loader;
        this.registry = registry;
    }

    public void iniciar(String properties) {
        for (QuestionPlugin plugin : loader.load(properties)) {
            try {
                registry.register(plugin);
            } catch (RuntimeException exception) {
                System.err.println("No se pudo registrar el plugin "
                        + plugin.getName() + ": " + exception.getMessage());
            }
        }
    }

    public void detener(String pluginId) {
        registry.unregister(pluginId);
    }

    public QuestionPlugin obtener(String name) { return registry.find(name); }

    public java.util.Collection<QuestionPlugin> listarPlugins() {
        return registry.list();
    }
}
