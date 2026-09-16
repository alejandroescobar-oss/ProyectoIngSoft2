package ui.swing;

import core.Kernel;
import model.Usuario;
import service.QuestionService;
import service.UsuarioService;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GridLayout;

public class DashboardView extends JFrame {
    private final Usuario usuario;
    private final UsuarioService usuarioService;
    private final QuestionService questionService;
    private final Kernel kernel;

    public DashboardView(Usuario usuario, UsuarioService usuarioService) {
        this(usuario, usuarioService, new QuestionService(new repository.QuestionRepositorySQLite()), null);
    }

    public DashboardView(Usuario usuario, UsuarioService usuarioService, Kernel kernel) {
        this(usuario, usuarioService, new QuestionService(new repository.QuestionRepositorySQLite()), kernel);
    }

    public DashboardView(Usuario usuario, UsuarioService usuarioService,
                         QuestionService questionService, Kernel kernel) {
        this.usuario = usuario;
        this.usuarioService = usuarioService;
        this.questionService = questionService;
        this.kernel = kernel;
        initComponents();
    }

    private void initComponents() {
        setTitle("Panel principal - " + usuario.getRol());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel(new GridLayout(0, 1, 8, 8));
        panel.add(new JLabel("Bienvenido, " + usuario.getNombreCompleto()));
        panel.add(new JLabel("Rol: " + usuario.getRol()));

        JButton preguntas = new JButton("Banco de preguntas");
        preguntas.addActionListener(e -> new PreguntasView(questionService).setVisible(true));
        panel.add(preguntas);

        if (kernel != null) {
            JButton plugins = new JButton("Ejecutar plugin");
            plugins.addActionListener(e -> new PluginExecutionView(kernel).setVisible(true));
            panel.add(plugins);
        }

        if (usuario.getRol() == model.Rol.ADMINISTRADOR) {
            JButton usuarios = new JButton("Gestionar usuarios");
            usuarios.addActionListener(e -> new GestionUsuariosView(usuarioService).setVisible(true));
            panel.add(usuarios);
        }

        JButton cerrar = new JButton("Cerrar sesión");
        cerrar.addActionListener(e -> {
            new LoginView(usuarioService, kernel).setVisible(true);
            dispose();
        });
        panel.add(cerrar);
        setContentPane(panel);
        setSize(420, 260);
        setLocationRelativeTo(null);
    }
}
