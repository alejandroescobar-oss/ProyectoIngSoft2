package core;

import core.contracts.QuestionPlugin;
import core.contracts.PluginCatalog;

import java.util.*;
public class PluginRegistry implements PluginCatalog {
    private final Map<String, QuestionPlugin> plugins = new LinkedHashMap<>();

    @Override
    public void register(QuestionPlugin plugin) {
        if (plugins.containsKey(plugin.getName())) {
            throw new IllegalStateException("Plugin duplicado: " + plugin.getName());
        }
        plugins.put(plugin.getName(), plugin);
    }

    @Override
    public void unregister(String name) {
        plugins.remove(name);
    }

    @Override
    public Collection<QuestionPlugin> list() { return List.copyOf(plugins.values()); }
    @Override
    public QuestionPlugin find(String name) { return plugins.get(name); }

    public void registrar(QuestionPlugin plugin) { register(plugin); }
    public void desregistrar(String name) { unregister(name); }
    public Collection<QuestionPlugin> listar() { return list(); }
    public QuestionPlugin obtener(String name) { return find(name); }
}
