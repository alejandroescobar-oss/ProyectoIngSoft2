package ui.swing;

import model.Question;
import model.QuestionStatus;
import service.QuestionService;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PreguntasView extends JFrame {
    private final QuestionService service;
    private final JComboBox<Question> questions = new JComboBox<>();
    private final JTextField id = new JTextField();
    private final JTextField name = new JTextField();
    private final JTextArea text = new JTextArea(4, 30);
    private final JTextField options = new JTextField();
    private final JComboBox<Integer> correctOption = new JComboBox<>();
    private final JComboBox<QuestionStatus> status = new JComboBox<>(QuestionStatus.values());
    private final StatisticsView statisticsView;
    private final PieChartView pieChartView;

    public PreguntasView(QuestionService service) {
        this.service = service;
        statisticsView = new StatisticsView(service);
        pieChartView = new PieChartView(service);
        initComponents();
        cargarPreguntas();
    }

    private void initComponents() {
        setTitle("Banco de preguntas Saber Pro");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        questions.addActionListener(e -> cargarPreguntaSeleccionada());
        id.setEditable(false);

        JPanel top = new JPanel();
        top.add(new JLabel("Pregunta:"));
        top.add(questions);
        JButton nueva = new JButton("Nueva");
        nueva.addActionListener(e -> limpiarFormulario());
        top.add(nueva);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        agregarCampo(form, "Id:", id, 0);
        agregarCampo(form, "Nombre:", name, 1);
        agregarCampo(form, "Texto:", new JScrollPane(text), 2);
        agregarCampo(form, "Opciones (separadas por |):", options, 3);
        agregarCampo(form, "Respuesta correcta:", correctOption, 4);
        agregarCampo(form, "Estado:", status, 5);

        JButton save = new JButton("Guardar");
        save.addActionListener(e -> guardar());
        JButton update = new JButton("Actualizar estado");
        update.addActionListener(e -> actualizarEstado());
        JPanel buttons = new JPanel();
        buttons.add(save);
        buttons.add(update);
        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.gridx = 1; buttonConstraints.gridy = 6;
        buttonConstraints.insets = new Insets(5, 5, 5, 5);
        form.add(buttons, buttonConstraints);

        JPanel observers = new JPanel(new GridLayout(1, 2));
        observers.add(statisticsView);
        observers.add(pieChartView);
        JSplitPane center = new JSplitPane(JSplitPane.VERTICAL_SPLIT, form, observers);
        center.setResizeWeight(0.62);
        add(top, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        setSize(900, 700);
        setLocationRelativeTo(null);
    }

    private void agregarCampo(JPanel panel, String label, java.awt.Component component, int row) {
        GridBagConstraints left = new GridBagConstraints();
        left.gridx = 0; left.gridy = row; left.anchor = GridBagConstraints.NORTHWEST;
        left.insets = new Insets(5, 5, 5, 5);
        panel.add(new JLabel(label), left);
        GridBagConstraints right = new GridBagConstraints();
        right.gridx = 1; right.gridy = row; right.weightx = 1; right.fill = GridBagConstraints.HORIZONTAL;
        right.insets = new Insets(5, 5, 5, 5);
        panel.add(component, right);
    }

    private void cargarPreguntas() {
        questions.setModel(new DefaultComboBoxModel<>(service.listarTodas().toArray(new Question[0])));
        if (questions.getItemCount() > 0) {
            questions.setSelectedIndex(0);
        } else {
            limpiarFormulario();
        }
    }

    private void cargarPreguntaSeleccionada() {
        Question question = (Question) questions.getSelectedItem();
        if (question == null) return;
        id.setText(String.valueOf(question.getId()));
        name.setText(question.getNombre());
        text.setText(question.getTexto());
        options.setText(String.join("|", question.getOpciones()));
        actualizarOpcionesCorrectas(question.getOpciones().size(), question.getRespuestaCorrecta());
        status.setSelectedItem(question.getEstado());
    }

    private void limpiarFormulario() {
        questions.setSelectedItem(null);
        id.setText("");
        name.setText("");
        text.setText("");
        options.setText("");
        actualizarOpcionesCorrectas(0, 0);
        status.setSelectedItem(QuestionStatus.BORRADOR);
    }

    private void actualizarOpcionesCorrectas(int size, int selected) {
        correctOption.removeAllItems();
        for (int i = 0; i < size; i++) correctOption.addItem(i);
        if (size > 0) correctOption.setSelectedItem(Math.min(selected, size - 1));
    }

    private List<String> leerOpciones() {
        return new ArrayList<>(Arrays.asList(options.getText().split("\\|", -1)));
    }

    private void guardar() {
        try {
            List<String> questionOptions = leerOpciones();
            if (name.getText().isBlank() || text.getText().isBlank() || questionOptions.isEmpty()
                    || questionOptions.get(0).isBlank()) {
                throw new IllegalArgumentException("Complete nombre, texto y opciones.");
            }
            int selectedOption = correctOption.getSelectedItem() instanceof Integer
                    ? (Integer) correctOption.getSelectedItem() : 0;
            actualizarOpcionesCorrectas(questionOptions.size(), selectedOption);
            service.guardar(new Question(name.getText().trim(), text.getText().trim(),
                    questionOptions, (Integer) correctOption.getSelectedItem(),
                    (QuestionStatus) status.getSelectedItem()));
            cargarPreguntas();
            JOptionPane.showMessageDialog(this, "Pregunta guardada correctamente.");
        } catch (RuntimeException exception) {
            JOptionPane.showMessageDialog(this, exception.getMessage(), "Error",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void actualizarEstado() {
        Question question = (Question) questions.getSelectedItem();
        if (question == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una pregunta.");
            return;
        }
        service.cambiarEstado(question, (QuestionStatus) status.getSelectedItem());
        cargarPreguntas();
        JOptionPane.showMessageDialog(this, "Estado actualizado.");
    }
}
