package ui.swing;

import model.QuestionStatus;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import service.QuestionService;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PieChartView extends JPanel implements PropertyChangeListener {
    private final QuestionService service;
    private final ChartPanel chartPanel;

    public PieChartView(QuestionService service) {
        this.service = service;
        service.addPropertyChangeListener(this);
        setLayout(new BorderLayout());
        chartPanel = new ChartPanel(crearGrafico());
        add(chartPanel, BorderLayout.CENTER);
    }

    /**
     * Construye el gráfico de distribución a partir del conteo actual
     * de preguntas por estado.
     *
     * @return gráfico con una sección para cada estado del ciclo de vida
     */
    private JFreeChart crearGrafico() {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        for (QuestionStatus status : QuestionStatus.values()) {
            dataset.setValue(status.getDisplayName(), service.listarPorEstado(status).size());
        }
        return ChartFactory.createPieChart("Distribución de preguntas", dataset, true, true, false);
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (QuestionService.STATUS_PROPERTY.equals(event.getPropertyName())) {
            chartPanel.setChart(crearGrafico());
        }
    }
}
