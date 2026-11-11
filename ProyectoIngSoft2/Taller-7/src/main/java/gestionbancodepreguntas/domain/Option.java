package gestionbancodepreguntas.domain;

public final class Option {
    private final String text;

    public Option(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("El texto de la opción es obligatorio.");
        }
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Option)) {
            return false;
        }
        Option option = (Option) other;
        return text.equals(option.text);
    }

    @Override
    public int hashCode() {
        return text.hashCode();
    }

    @Override
    public String toString() {
        return text;
    }
}
