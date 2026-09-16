package core;

import core.contracts.QuestionPlugin;
import core.contracts.PluginSource;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class PluginLoader implements PluginSource {
    private final Map<String, URLClassLoader> classLoaders = new HashMap<>();

    /** Carga todos los plugins declarados en plugins.properties. */
    @Override
    public List<QuestionPlugin> load(String rutaProperties) {
        Properties props = new Properties();
        try {
            File file = new File(rutaProperties);
            if (file.isFile()) {
                try (InputStream fileInput = new java.io.FileInputStream(file)) {
                    props.load(fileInput);
                }

            } else {
                try (InputStream resource = getClass().getClassLoader()
                        .getResourceAsStream(rutaProperties)) {
                    if (resource == null) return List.of();
                    props.load(resource);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error leyendo plugins.properties", e);
        }

        List<QuestionPlugin> plugins = new ArrayList<>();
        Set<String> ids = new HashSet<>();
        for (String key : props.stringPropertyNames()) {
            if (key.endsWith(".jar")) {
                ids.add(key.substring(0, key.length() - 4)); // "plugin.X"
            }
        }

        for (String id : ids) {
            if (!Boolean.parseBoolean(props.getProperty(id + ".enabled", "true")))
                continue;
            try {
                QuestionPlugin p = cargarPlugin(
                        props.getProperty(id + ".jar"),
                        props.getProperty(id + ".class"));
                plugins.add(p);
            } catch (Exception e) {
                // Un plugin roto NO debe tumbar el núcleo
                System.err.println("Plugin " + id + " falló: " + e.getMessage());
            }
        }
        return plugins;
    }

    public List<QuestionPlugin> cargarTodos(String rutaProperties) {
        return load(rutaProperties);
    }

    private QuestionPlugin cargarPlugin(String rutaJar, String clase) throws Exception {
        File jar = new File(rutaJar);
        if (!jar.exists()) throw new IllegalStateException("JAR no encontrado: " + rutaJar);

        URLClassLoader loader = new URLClassLoader(
                new URL[]{jar.toURI().toURL()},
                getClass().getClassLoader());   // parent = classpath del núcleo
        classLoaders.put(clase, loader);

        Class<?> clazz = Class.forName(clase, true, loader);
        if (!QuestionPlugin.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("La clase no implementa QuestionPlugin: " + clase);
        }
        return (QuestionPlugin) clazz.getDeclaredConstructor().newInstance();
    }

    /** Permite recargar un plugin sin reiniciar la app (hot-swap). */
    public void descargar(String clase) throws Exception {
        URLClassLoader l = classLoaders.remove(clase);
        if (l != null) l.close();
    }
}
