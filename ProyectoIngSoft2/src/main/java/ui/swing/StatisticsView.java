package ui.swing;

import model.QuestionStatus;
import service.QuestionService;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EnumMap;
import java.util.Map;
import java.util.StringJoiner;

public class StatisticsView extends JPanel implements PropertyChangeListener {
    private final QuestionService service;
    private final JLabel statsLabel = new JLabel();

    public StatisticsView(QuestionService service) {
        this.service = service;
        service.addPropertyChangeListener(this);
        setLayout(new BorderLayout());
        add(statsLabel, BorderLayout.CENTER);
        actualizarEstadisticas();
    }

    /**
     * Actualiza el conteo de preguntas correspondiente a cada estado
     * del ciclo de vida.
     */
    private void actualizarEstadisticas() {
        Map<QuestionStatus, Integer> counts = new EnumMap<>(QuestionStatus.class);
        for (QuestionStatus status : QuestionStatus.values()) {
            counts.put(status, service.listarPorEstado(status).size());
        }
        StringJoiner lines = new StringJoiner("<br>", "<html>", "</html>");
        for (QuestionStatus status : QuestionStatus.values()) {
            lines.add(status.getDisplayName() + ": " + counts.get(status));
        }
        statsLabel.setText(lines.toString());
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (QuestionService.STATUS_PROPERTY.equals(event.getPropertyName())) {
            actualizarEstadisticas();
        }
    }
}
