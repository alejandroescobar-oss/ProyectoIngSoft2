package model;


public class Usuario {

    private int id;
    private String login;
    private String nombreCompleto;
    private Rol rol;
    private EstadoUsuario estado;
    private String passwordHash;

    public Usuario(
            String login,
            String nombreCompleto,
            Rol rol,
            EstadoUsuario estado,
            String passwordHash) {

        this.login = login;
        this.nombreCompleto = nombreCompleto;
        this.rol = rol;
        this.estado = estado;
        this.passwordHash = passwordHash;
    }

    public Usuario(
            int id,
            String login,
            String nombreCompleto,
            Rol rol,
            EstadoUsuario estado,
            String passwordHash) {

        this.id = id;
        this.login = login;
        this.nombreCompleto = nombreCompleto;
        this.rol = rol;
        this.estado = estado;
        this.passwordHash = passwordHash;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public Rol getRol() {
        return rol;
    }

    public EstadoUsuario getEstado() {
        return estado;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public void setEstado(EstadoUsuario estado) {
        this.estado = estado;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    @Override
public String toString() {

    return login
            + " | "
            + nombreCompleto
            + " | Rol: "
            + rol
            + " | Estado: "
            + estado;
}
}
