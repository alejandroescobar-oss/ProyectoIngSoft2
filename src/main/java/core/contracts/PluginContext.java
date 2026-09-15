package core.contracts;

import core.BancoPreguntas;

public interface PluginContext {
    BancoPreguntas getBancoPreguntas();     // acceso al almacén
    Pipeline getPipeline();                 // pipeline configurado por el núcleo
    void log(String mensaje);               // logging centralizado
    <T> T getServicio(Class<T> tipo);       // lookup de servicios del núcleo
}
