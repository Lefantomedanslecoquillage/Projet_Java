package fr.school.smartenergy.exception;

public class ImportException extends AppException {

    public ImportException() {
        super();
    }

    public ImportException(String message) {
        super(message);
    }

    public ImportException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImportException(Throwable cause) {
        super(cause);
    }
}