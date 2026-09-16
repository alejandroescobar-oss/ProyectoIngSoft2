package core.contracts;

import java.util.Collection;

public interface PluginCatalog {
    void register(QuestionPlugin plugin);
    void unregister(String name);
    QuestionPlugin find(String name);
    Collection<QuestionPlugin> list();
}
