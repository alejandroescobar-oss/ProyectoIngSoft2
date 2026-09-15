package core.contracts;

import core.model.PreguntaNueva;
import core.model.ResultadoPipeline;

public interface Pipeline {
    ResultadoPipeline ejecutar(PreguntaNueva entrada);
}
