package validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class PasswordValidatorTest {

    private final PasswordValidator validator =
            new PasswordValidator();

    @Test
    void debeAceptarPasswordValida() {

        assertTrue(
                validator.esValida("Abcdef1!")
        );
    }

    @Test
    void debeRechazarPasswordMenorASeisCaracteres() {

        assertFalse(
                validator.esValida("Ab1!")
        );
    }

    @Test
    void debeRechazarPasswordSinMayuscula() {

        assertFalse(
                validator.esValida("abcdef1!")
        );
    }

    @Test
    void debeRechazarPasswordSinDigito() {

        assertFalse(
                validator.esValida("Abcdef!")
        );
    }

    @Test
    void debeRechazarPasswordSinCaracterEspecial() {

        assertFalse(
                validator.esValida("Abcdef1")
        );
    }
}