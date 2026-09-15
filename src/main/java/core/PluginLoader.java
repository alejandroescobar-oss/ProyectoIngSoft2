package core;

import core.contracts.Plugin;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class PluginLoader {
    private final Map<String, URLClassLoader> classLoaders = new HashMap<>();

    /** Carga todos los plugins declarados en plugins.properties. */
    public List<Plugin> cargarTodos(String rutaProperties) {
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

        List<Plugin> plugins = new ArrayList<>();
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
                Plugin p = cargarPlugin(
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

    private Plugin cargarPlugin(String rutaJar, String clase) throws Exception {
        File jar = new File(rutaJar);
        if (!jar.exists()) throw new IllegalStateException("JAR no encontrado: " + rutaJar);

        URLClassLoader loader = new URLClassLoader(
                new URL[]{jar.toURI().toURL()},
                getClass().getClassLoader());   // parent = classpath del núcleo
        classLoaders.put(clase, loader);

        Class<?> clazz = Class.forName(clase, true, loader);
        return (Plugin) clazz.getDeclaredConstructor().newInstance();
    }

    /** Permite recargar un plugin sin reiniciar la app (hot-swap). */
    public void descargar(String clase) throws Exception {
        URLClassLoader l = classLoaders.remove(clase);
        if (l != null) l.close();
    }
}
