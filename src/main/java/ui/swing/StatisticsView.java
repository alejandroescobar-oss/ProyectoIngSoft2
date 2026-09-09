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

    private void actualizarEstadisticas() {
        Map<QuestionStatus, Integer> counts = new EnumMap<>(QuestionStatus.class);
        for (QuestionStatus status : QuestionStatus.values()) {
            counts.put(status, service.listarPorEstado(status).size());
        }
        statsLabel.setText(String.format("<html>Borrador: %d<br>Pendiente revisión: %d<br>Eliminada: %d</html>",
                counts.get(QuestionStatus.BORRADOR),
                counts.get(QuestionStatus.PENDIENTE_REVISION),
                counts.get(QuestionStatus.ELIMINADA)));
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (QuestionService.STATUS_PROPERTY.equals(event.getPropertyName())) {
            actualizarEstadisticas();
        }
    }
}
