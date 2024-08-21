package exceptions;

public class NoPatientException extends Exception {
    public NoPatientException() {
        super("There are no patients in the queue.");
    }
}
