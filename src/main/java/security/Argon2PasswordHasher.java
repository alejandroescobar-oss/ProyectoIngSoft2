package security;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

public class Argon2PasswordHasher implements PasswordHasher {

    private final Argon2PasswordEncoder encoder;

    public Argon2PasswordHasher() {
        this.encoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }

    @Override
    public String hash(String password) {
        return encoder.encode(password);
    }

    @Override
    public boolean verificar(String password, String hash) {
        return encoder.matches(password, hash);
    }
}