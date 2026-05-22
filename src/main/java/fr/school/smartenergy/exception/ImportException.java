package fr.school.smartenergy.exception;

/** Thrown when a CSV import fails. */
public class ImportException extends AppException {

    public ImportException(String message) {
        super(message);
    }

    public ImportException(String message, Throwable cause) {
        super(message, cause);
    }
}
