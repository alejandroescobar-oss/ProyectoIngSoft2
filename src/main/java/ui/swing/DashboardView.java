package ui.swing;

import model.Usuario;
import repository.QuestionRepositorySQLite;
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

    public DashboardView(Usuario usuario, UsuarioService usuarioService) {
        this.usuario = usuario;
        this.usuarioService = usuarioService;
        initComponents();
    }

    private void initComponents() {
        setTitle("Panel principal - " + usuario.getRol());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel(new GridLayout(0, 1, 8, 8));
        panel.add(new JLabel("Bienvenido, " + usuario.getNombreCompleto()));
        panel.add(new JLabel("Rol: " + usuario.getRol()));

        JButton preguntas = new JButton("Banco de preguntas");
        preguntas.addActionListener(e -> new PreguntasView(
                new QuestionService(new QuestionRepositorySQLite())).setVisible(true));
        panel.add(preguntas);

        if (usuario.getRol() == model.Rol.ADMINISTRADOR) {
            JButton usuarios = new JButton("Gestionar usuarios");
            usuarios.addActionListener(e -> new GestionUsuariosView(usuarioService).setVisible(true));
            panel.add(usuarios);
        }

        JButton cerrar = new JButton("Cerrar sesión");
        cerrar.addActionListener(e -> {
            new LoginView(usuarioService).setVisible(true);
            dispose();
        });
        panel.add(cerrar);
        setContentPane(panel);
        setSize(420, 260);
        setLocationRelativeTo(null);
    }
}
