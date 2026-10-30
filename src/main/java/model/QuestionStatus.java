package model;

/**
 * Estados del ciclo de vida de una pregunta Saber Pro.
 *
 * <p>El estado archivado reemplaza la eliminación física para conservar
 * la trazabilidad de la pregunta.</p>
 */
public enum QuestionStatus {
    BORRADOR,
    PENDIENTE_REVISION,
    EN_REVISION,
    APROBADA,
    RECHAZADA,
    PUBLICADA,
    ARCHIVADA;

    /**
     * Obtiene la etiqueta en español que presenta la aplicación de escritorio.
     *
     * @return nombre visible del estado
     */
    public String getDisplayName() {
        return switch (this) {
            case BORRADOR -> "Borrador";
            case PENDIENTE_REVISION -> "Pendiente de revisión";
            case EN_REVISION -> "En revisión";
            case APROBADA -> "Aprobada";
            case RECHAZADA -> "Rechazada";
            case PUBLICADA -> "Publicada";
            case ARCHIVADA -> "Archivada";
        };
    }

    /**
     * Representa el estado con la etiqueta visible en español.
     *
     * @return etiqueta del estado para presentación
     */
    @Override
    public String toString() {
        return getDisplayName();
    }
}
