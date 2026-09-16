package ui.swing;

import core.Kernel;
import core.contracts.QuestionPlugin;
import core.model.Question;
import core.model.QuestionRequest;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.List;

public class PluginExecutionView extends JFrame {
    private final Kernel kernel;
    private final JComboBox<String> pluginCombo = new JComboBox<>();
    private final JTextField nombre = new JTextField();
    private final JTextArea texto = new JTextArea(4, 30);
    private final JTextField opciones = new JTextField();
    private final JTextField correcta = new JTextField("0");

    public PluginExecutionView(Kernel kernel) {
        this.kernel = kernel;
        initComponents();
        cargarPlugins();
    }

    private void initComponents() {
        setTitle("Ejecutar plugin");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
        form.add(new JLabel("Plugin:"));
        form.add(pluginCombo);
        form.add(new JLabel("Nombre:"));
        form.add(nombre);
        form.add(new JLabel("Texto:"));
        form.add(new JScrollPane(texto));
        form.add(new JLabel("Opciones separadas por |:"));
        form.add(opciones);
        form.add(new JLabel("Respuesta correcta (índice):"));
        form.add(correcta);

        JButton ejecutar = new JButton("Ejecutar");
        ejecutar.addActionListener(e -> ejecutarPlugin());
        setLayout(new BorderLayout(8, 8));
        add(form, BorderLayout.CENTER);
        add(ejecutar, BorderLayout.SOUTH);
        setSize(600, 450);
        setLocationRelativeTo(null);
    }

    private void cargarPlugins() {
        for (QuestionPlugin plugin : kernel.getLifecycle().listarPlugins()) {
            pluginCombo.addItem(plugin.getName());
        }
    }

    private void ejecutarPlugin() {
        String pluginId = (String) pluginCombo.getSelectedItem();
        if (pluginId == null) {
            JOptionPane.showMessageDialog(this, "No hay plugins cargados.",
                    "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            Integer.parseInt(correcta.getText().trim());
            List<String> opcionesEntrada = Arrays.stream(opciones.getText().split("\\|", -1))
                    .map(String::trim)
                    .toList();
            QuestionRequest request = new QuestionRequest(
                    nombre.getText().trim(), texto.getText().trim(), "MULTIPLE_CHOICE",
                    opcionesEntrada, Integer.parseInt(correcta.getText().trim()));
            Question question = kernel.generateQuestion(pluginId, request);
            JOptionPane.showMessageDialog(this,
                    "Pregunta procesada y almacenada.\nID: " + question.getId());
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(this, "El índice debe ser un número entero.",
                    "Entrada inválida", JOptionPane.WARNING_MESSAGE);
        } catch (RuntimeException exception) {
            JOptionPane.showMessageDialog(this, exception.getMessage(),
                    "Error ejecutando plugin", JOptionPane.ERROR_MESSAGE);
        }
    }
}
