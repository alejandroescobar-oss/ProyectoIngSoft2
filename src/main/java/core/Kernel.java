package core;

import core.contracts.PluginContext;
import pipeline.filters.ClassificationFilter;
import pipeline.filters.ContentValidationFilter;
import pipeline.filters.CorrectAnswerValidatorFilter;
import pipeline.filters.OptionsValidatorFilter;
import pipeline.PipelineImpl;

import java.util.List;
import java.util.Map;

public class Kernel {
    private final LifecycleManager lifecycle;
    private final BancoPreguntas banco;

    public Kernel() {
        this.banco = new BancoPreguntas();
        PipelineImpl pipeline = new PipelineImpl(List.of(
                new ContentValidationFilter(),
                new OptionsValidatorFilter(),
                new ClassificationFilter(),
                new CorrectAnswerValidatorFilter()
        ));
        PluginContext ctx = new DefaultPluginContext(banco, pipeline, Map.of());
        PluginRegistry registry = new PluginRegistry(ctx);
        this.lifecycle = new LifecycleManager(new PluginLoader(), registry);
    }

    public void arrancar() {
        lifecycle.iniciar("plugins.properties");
    }

    public LifecycleManager getLifecycle() { return lifecycle; }
    public BancoPreguntas getBanco()       { return banco; }
}
