package example.sportal.exceptions;

public class WrongCredentialsException extends RuntimeException {

    public WrongCredentialsException(String messages){
        super(messages);
    }
}
