package example.sportal.exceptions;

public class FailedCredentialsException extends RuntimeException {

    public FailedCredentialsException(String massages) {
        super(massages);
    }
}
