package security;

public interface PasswordHasher {

    String hash(String password);

    boolean verificar(String password, String hash);
}