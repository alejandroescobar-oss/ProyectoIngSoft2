package core;

import core.contracts.Pipeline;
import java.util.Map;
import core.model.Question;
import core.model.QuestionRequest;
import core.model.PreguntaNueva;
import core.model.ResultadoPipeline;
import core.contracts.QuestionPlugin;
import java.util.LinkedHashMap;
import java.util.List;
import pipeline.PipelineImpl;
import pipeline.filters.ClassificationFilter;
import pipeline.filters.ContentValidationFilter;
import pipeline.filters.CorrectAnswerValidatorFilter;
import pipeline.filters.OptionsValidatorFilter;

public class Kernel {
    private final LifecycleManager lifecycle;
    private final BancoPreguntas banco;
    private final Pipeline pipeline;
    private final Map<String, Question> questions = new LinkedHashMap<>();

    public Kernel() {
        this(new BancoPreguntas(), new PluginLoader(), new PluginRegistry(), crearPipeline());
    }

    public Kernel(BancoPreguntas banco, PluginLoader loader, PluginRegistry registry) {
        this(banco, loader, registry, crearPipeline());
    }

    public Kernel(BancoPreguntas banco, PluginLoader loader,
                  PluginRegistry registry, Pipeline pipeline) {
        this.banco = banco;
        this.pipeline = pipeline;
        this.lifecycle = new LifecycleManager(loader, registry);
    }

    public void arrancar() {
        lifecycle.iniciar("plugins.properties");
    }

    public LifecycleManager getLifecycle() { return lifecycle; }
    public BancoPreguntas getBanco()       { return banco; }
    public Map<String, Question> getQuestions() { return Map.copyOf(questions); }

    public Question generateQuestion(String pluginName, QuestionRequest request) {
        QuestionPlugin plugin = lifecycle.obtener(pluginName);
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin no registrado: " + pluginName);
        }
        if (!plugin.supports(request.getType())) {
            throw new IllegalArgumentException(
                    "El plugin " + pluginName + " no soporta el tipo " + request.getType());
        }
        ResultadoPipeline resultado = pipeline.ejecutar(new PreguntaNueva(
                request.getTitle(),
                request.getContent(),
                request.getOptions(),
                request.getCorrectAnswer()
        ));
        if (!resultado.isExitoso()) {
            throw new IllegalArgumentException(String.join("\n", resultado.getErrores()));
        }
        Question question = plugin.generate(request);
        if (question == null) {
            throw new IllegalStateException("El plugin " + pluginName + " no generó una pregunta.");
        }
        questions.put(question.getId(), question);
        banco.almacenar(question, request);
        return question;
    }

    private static Pipeline crearPipeline() {
        return new PipelineImpl(List.of(
                new ContentValidationFilter(),
                new OptionsValidatorFilter(),
                new ClassificationFilter(),
                new CorrectAnswerValidatorFilter()
        ));
    }
}
