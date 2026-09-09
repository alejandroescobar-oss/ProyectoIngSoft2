package ui.swing;

import model.Usuario;
import service.UsuarioService;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;

public class GestionUsuariosView extends JFrame {
    private final UsuarioService service;
    private final DefaultListModel<Usuario> model = new DefaultListModel<>();
    private final JList<Usuario> users = new JList<>(model);

    public GestionUsuariosView(UsuarioService service) {
        this.service = service;
        setTitle("Gestión de usuarios");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel actions = new JPanel();
        JButton edit = new JButton("Editar");
        edit.addActionListener(e -> editarSeleccionado());
        JButton delete = new JButton("Eliminar");
        delete.addActionListener(e -> eliminarSeleccionado());
        actions.add(edit); actions.add(delete);
        add(new JScrollPane(users), BorderLayout.CENTER);
        add(actions, BorderLayout.SOUTH);
        refrescar();
        setSize(600, 400);
        setLocationRelativeTo(null);
    }

    private void refrescar() {
        model.clear();
        service.listarUsuarios().forEach(model::addElement);
    }

    private void editarSeleccionado() {
        Usuario user = users.getSelectedValue();
        if (user == null) return;
        new EditarUsuarioView(user, service, this::refrescar).setVisible(true);
    }

    private void eliminarSeleccionado() {
        Usuario user = users.getSelectedValue();
        if (user == null) return;
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar el usuario seleccionado?",
                "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            service.eliminarUsuario(user.getId());
            refrescar();
        }
    }
}
