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

    // Excepción específica de archivo no encontrado
    public static class FileNotFoundException extends SpecificException {
        public FileNotFoundException() {
            super();
        }

        public FileNotFoundException(String message) {
            super(message);
        }

        public FileNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }

        public FileNotFoundException(Throwable cause) {
            super(cause);
        }
    }

    // Excepción específica de entrada/salida
    public static class IOException extends SpecificException {
        public IOException() {
            super();
        }

        public IOException(String message) {
            super(message);
        }

        public IOException(String message, Throwable cause) {
            super(message, cause);
        }

        public IOException(Throwable cause) {
            super(cause);
        }
    }

    // Excepción específica de división por cero
    public static class DivideByZeroException extends SpecificException {
        public DivideByZeroException() {
            super();
        }

        public DivideByZeroException(String message) {
            super(message);
        }

        public DivideByZeroException(String message, Throwable cause) {
            super(message, cause);
        }

        public DivideByZeroException(Throwable cause) {
            super(cause);
        }
    }

    // Agrega aquí más excepciones comunes según tus necesidades
}
