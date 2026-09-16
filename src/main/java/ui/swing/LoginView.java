package ui.swing;

import core.Kernel;
import model.Usuario;
import repository.QuestionRepositorySQLite;
import service.QuestionService;
import service.UsuarioService;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class LoginView extends JFrame {
    private final UsuarioService usuarioService;
    private final QuestionService questionService;
    private final Kernel kernel;
    private final JTextField loginField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);

    public LoginView(UsuarioService usuarioService) {
        this(usuarioService, new QuestionService(new QuestionRepositorySQLite()), null);
    }

    public LoginView(UsuarioService usuarioService, Kernel kernel) {
        this(usuarioService, new QuestionService(new QuestionRepositorySQLite()), kernel);
    }

    public LoginView(UsuarioService usuarioService, QuestionService questionService, Kernel kernel) {
        this.usuarioService = usuarioService;
        this.questionService = questionService;
        this.kernel = kernel;
        initComponents();
    }

    private void initComponents() {
        setTitle("Taller 2 - Inicio de sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(crearPanel());
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel crearPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        panel.add(new JLabel("INICIAR SESIÓN"), c);
        c.gridwidth = 1;
        c.gridx = 0; c.gridy++;
        panel.add(new JLabel("Usuario:"), c);
        c.gridx = 1;
        panel.add(loginField, c);
        c.gridx = 0; c.gridy++;
        panel.add(new JLabel("Contraseña:"), c);
        c.gridx = 1;
        panel.add(passwordField, c);

        JButton loginButton = new JButton("Iniciar sesión");
        loginButton.addActionListener(e -> iniciarSesion());
        JButton registerButton = new JButton("Registrarse");
        registerButton.addActionListener(e -> {
            new RegistroView(usuarioService).setVisible(true);
            dispose();
        });
        c.gridx = 0; c.gridy++;
        panel.add(loginButton, c);
        c.gridx = 1;
        panel.add(registerButton, c);
        return panel;
    }

    private void iniciarSesion() {
        try {
            Usuario usuario = usuarioService.iniciarSesion(
                    loginField.getText().trim(),
                    new String(passwordField.getPassword()));
            new DashboardView(usuario, usuarioService, questionService, kernel).setVisible(true);
            dispose();
        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(this, exception.getMessage(),
                    "No se pudo iniciar sesión", JOptionPane.WARNING_MESSAGE);
        }
    }
}
