package net.cero.seguridad.utilidades;

public class SpecificException extends Exception {

    public SpecificException() {
        super();
    }

    public SpecificException(String message) {
        super(message);
    }

    public SpecificException(String message, Throwable cause) {
        super(message, cause);
    }

    public SpecificException(Throwable cause) {
        super(cause);
    }

    public static class Generico extends SpecificException {
        public  Generico() {
            super();
        }

        public  Generico(String message) {
            super(message);
        }

        public  Generico(String message, Throwable cause) {
            super(message, cause);
        }

        public  Generico(Throwable cause) {
            super(cause);
        }
    }}
