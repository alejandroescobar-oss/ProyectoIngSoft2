package core.contracts;

import core.model.Question;
import core.model.QuestionRequest;

/**
 * Contrato único para los plugins que generan preguntas.
 */
public interface QuestionPlugin {
    String getName();

    boolean supports(String type);

    Question generate(QuestionRequest request);
}
