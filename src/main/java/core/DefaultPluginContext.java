package core;

import core.contracts.Pipeline;
import core.contracts.PluginContext;

import java.util.Map;

public final class DefaultPluginContext implements PluginContext {
    private final BancoPreguntas bancoPreguntas;
    private final Pipeline pipeline;
    private final Map<Class<?>, Object> servicios;

    public DefaultPluginContext(BancoPreguntas bancoPreguntas, Pipeline pipeline,
                                Map<Class<?>, Object> servicios) {
        this.bancoPreguntas = bancoPreguntas;
        this.pipeline = pipeline;
        this.servicios = Map.copyOf(servicios);
    }

    @Override
    public BancoPreguntas getBancoPreguntas() {
        return bancoPreguntas;
    }

    @Override
    public Pipeline getPipeline() {
        return pipeline;
    }

    @Override
    public void log(String mensaje) {
        System.out.println("[plugin] " + mensaje);
    }

    @Override
    public <T> T getServicio(Class<T> tipo) {
        Object servicio = servicios.get(tipo);
        return servicio == null ? null : tipo.cast(servicio);
    }
}
