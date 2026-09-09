package ui.swing;

import model.Usuario;
import service.UsuarioService;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;

public class ListaUsuariosView extends JFrame {
    public ListaUsuariosView(UsuarioService service) {
        setTitle("Usuarios registrados");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        DefaultListModel<Usuario> model = new DefaultListModel<>();
        service.listarUsuarios().forEach(model::addElement);
        add(new JScrollPane(new JList<>(model)), BorderLayout.CENTER);
        setSize(560, 360);
        setLocationRelativeTo(null);
    }
}
