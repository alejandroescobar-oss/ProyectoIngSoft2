package core;

import core.contracts.Plugin;
import core.contracts.PluginContext;

import java.util.*;
public class PluginRegistry {
    private final Map<String, Plugin> plugins = new LinkedHashMap<>();
    private final PluginContext context;

    public PluginRegistry(PluginContext context) { this.context = context; }

    public void registrar(Plugin p) {
        if (plugins.containsKey(p.getId()))
            throw new IllegalStateException("Plugin duplicado: " + p.getId());
        p.inicializar(context);
        plugins.put(p.getId(), p);
    }

    public void desregistrar(String id) {
        Plugin p = plugins.remove(id);
        if (p != null) p.detener();
    }

    public Collection<Plugin> listar() { return plugins.values(); }
    public Plugin obtener(String id) { return plugins.get(id); }
}
