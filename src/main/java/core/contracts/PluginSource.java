package core.contracts;

import java.util.List;

public interface PluginSource {
    List<QuestionPlugin> load(String propertiesPath);
}
