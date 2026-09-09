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

    private JFreeChart crearGrafico() {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        dataset.setValue("Borrador", service.listarPorEstado(QuestionStatus.BORRADOR).size());
        dataset.setValue("Pendiente revisión", service.listarPorEstado(QuestionStatus.PENDIENTE_REVISION).size());
        dataset.setValue("Eliminada", service.listarPorEstado(QuestionStatus.ELIMINADA).size());
        return ChartFactory.createPieChart("Distribución de preguntas", dataset, true, true, false);
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (QuestionService.STATUS_PROPERTY.equals(event.getPropertyName())) {
            chartPanel.setChart(crearGrafico());
        }
    }
}
