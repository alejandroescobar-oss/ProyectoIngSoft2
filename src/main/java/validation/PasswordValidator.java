package validation;

public class PasswordValidator {

    public boolean esValida(String password) {

        if (password == null || password.length() < 6) {
            return false;
        }

        boolean tieneMayuscula = false;
        boolean tieneDigito = false;
        boolean tieneEspecial = false;

        for (char c : password.toCharArray()) {

            if (Character.isUpperCase(c)) {
                tieneMayuscula = true;
            }

            if (Character.isDigit(c)) {
                tieneDigito = true;
            }

            if (!Character.isLetterOrDigit(c)) {
                tieneEspecial = true;
            }
        }

        return tieneMayuscula && tieneDigito && tieneEspecial;
    }
}